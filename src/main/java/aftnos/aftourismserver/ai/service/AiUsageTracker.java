package aftnos.aftourismserver.ai.service;

import aftnos.aftourismserver.ai.dto.AiModelStatDTO;
import aftnos.aftourismserver.ai.dto.AiServiceStatusDTO;
import aftnos.aftourismserver.ai.dto.AiSettingsDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 记录模型调用指标，供看板和告警使用。
 */
@Component
public class AiUsageTracker {

    private final ConcurrentHashMap<String, ModelUsage> usages = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ProviderHealth> providerHealth = new ConcurrentHashMap<>();
    private final ConcurrentLinkedDeque<AiServiceStatusDTO.ErrorEntry> recentErrors = new ConcurrentLinkedDeque<>();
    private final AiAlertService alertService;

    public AiUsageTracker(AiAlertService alertService) {
        this.alertService = alertService;
    }

    public void recordSuccess(String model, String provider, long latencyMs) {
        ModelUsage usage = usages.computeIfAbsent(model, key -> new ModelUsage(model, provider));
        usage.success.add(1);
        usage.total.add(1);
        usage.totalLatency.add(latencyMs);
        usage.lastCall = Instant.now();
        applyProvider(provider, latencyMs, true, null);
        alertService.evaluate(model, provider, usage.toStat());
    }

    public void recordFailure(String model, String provider, long latencyMs, String error) {
        ModelUsage usage = usages.computeIfAbsent(model, key -> new ModelUsage(model, provider));
        usage.failure.add(1);
        usage.total.add(1);
        usage.totalLatency.add(latencyMs);
        usage.lastCall = Instant.now();
        applyProvider(provider, latencyMs, false, error);
        pushError(provider, model, error);
        alertService.evaluate(model, provider, usage.toStat());
    }

    public List<AiModelStatDTO> snapshot(String modelFilter, String providerFilter) {
        return usages.values().stream()
                .map(ModelUsage::toStat)
                .filter(stat -> !StringUtils.hasText(modelFilter) || Objects.equals(stat.getModel(), modelFilter))
                .filter(stat -> !StringUtils.hasText(providerFilter) || Objects.equals(stat.getProvider(), providerFilter))
                .sorted(Comparator.comparing(AiModelStatDTO::getModel))
                .collect(Collectors.toList());
    }

    public List<AiServiceStatusDTO.ServiceHealth> buildServiceHealth(AiSettingsDTO settings) {
        List<AiServiceStatusDTO.ServiceHealth> healthList = new ArrayList<>();
        for (AiSettingsDTO.Provider provider : settings.getProviders()) {
            ProviderHealth health = providerHealth.getOrDefault(provider.getCode(), new ProviderHealth());
            AiServiceStatusDTO.ServiceHealth dto = new AiServiceStatusDTO.ServiceHealth();
            dto.setProvider(provider.getName());
            dto.setEndpoint(provider.getEndpoint());
            dto.setStatus(health.status);
            dto.setLatencyMs(health.latencyMs);
            dto.setCheckedAt(health.checkedAt);
            healthList.add(dto);
        }
        return healthList;
    }

    public List<AiServiceStatusDTO.ErrorEntry> recentErrors(int limit) {
        return recentErrors.stream().limit(limit).collect(Collectors.toList());
    }

    public void recordProviderTest(String provider, long latency, boolean success, String message) {
        applyProvider(provider, latency, success, message);
        if (!success) {
            pushError(provider, null, message);
        }
    }

    private void pushError(String provider, String model, String message) {
        AiServiceStatusDTO.ErrorEntry entry = new AiServiceStatusDTO.ErrorEntry();
        entry.setProvider(provider);
        entry.setModel(model);
        entry.setMessage(message);
        entry.setOccurredAt(Instant.now());
        recentErrors.addFirst(entry);
        while (recentErrors.size() > 50) {
            recentErrors.removeLast();
        }
    }

    private void applyProvider(String provider, long latency, boolean success, String error) {
        if (!StringUtils.hasText(provider)) {
            return;
        }
        ProviderHealth health = providerHealth.computeIfAbsent(provider, key -> new ProviderHealth());
        health.checkedAt = Instant.now();
        health.latencyMs = latency;
        health.status = success ? "UP" : "DOWN";
        if (!success && error != null) {
            health.lastError = error;
        }
    }

    public Map<String, ModelUsage> rawUsages() {
        return usages;
    }

    static class ModelUsage {
        private final String model;
        private final String provider;
        private final LongAdder total = new LongAdder();
        private final LongAdder success = new LongAdder();
        private final LongAdder failure = new LongAdder();
        private final LongAdder totalLatency = new LongAdder();
        private volatile Instant lastCall;

        ModelUsage(String model, String provider) {
            this.model = model;
            this.provider = provider;
        }

        AiModelStatDTO toStat() {
            AiModelStatDTO dto = new AiModelStatDTO();
            dto.setModel(model);
            dto.setProvider(provider);
            long totalCount = total.sum();
            dto.setTotal(totalCount);
            dto.setSuccess(success.sum());
            dto.setFailure(failure.sum());
            double avg = totalCount == 0 ? 0D : (double) totalLatency.sum() / totalCount;
            dto.setAvgLatencyMs(avg);
            double errorRate = totalCount == 0 ? 0D : (double) failure.sum() / totalCount;
            dto.setErrorRate(errorRate);
            return dto;
        }
    }

    private static class ProviderHealth {
        private String status = "UNKNOWN";
        private Long latencyMs;
        private Instant checkedAt;
        private String lastError;
    }
}
