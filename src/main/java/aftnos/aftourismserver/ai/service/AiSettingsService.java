package aftnos.aftourismserver.ai.service;

import aftnos.aftourismserver.ai.dto.AiModelDefaultRequest;
import aftnos.aftourismserver.ai.dto.AiModelStatDTO;
import aftnos.aftourismserver.ai.dto.AiSettingsDTO;
import aftnos.aftourismserver.ai.dto.AiServiceStatusDTO;
import aftnos.aftourismserver.ai.safety.AiSafetyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 负责管理 AI 运行时配置以及配置相关的校验逻辑。
 */
@Service
public class AiSettingsService {

    private static final String SETTINGS_KEY = "ai:settings";
    private final RedisTemplate<String, Object> redisTemplate;

    public AiSettingsService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public synchronized AiSettingsDTO getSettings() {
        AiSettingsDTO settings = (AiSettingsDTO) redisTemplate.opsForValue().get(SETTINGS_KEY);
        if (settings == null) {
            settings = new AiSettingsDTO();
            settings.setDefaultChatModel("gpt-4o-mini");
            settings.setFallbackChatModel("gpt-4o-mini");
            redisTemplate.opsForValue().set(SETTINGS_KEY, settings);
        }
        normalize(settings);
        return settings;
    }

    public synchronized void saveSettings(AiSettingsDTO settings) {
        normalize(settings);
        redisTemplate.opsForValue().set(SETTINGS_KEY, settings);
    }

    private void normalize(AiSettingsDTO settings) {
        if (settings.getProviders() == null) {
            settings.setProviders(new ArrayList<>());
        }
        if (settings.getModels() == null) {
            settings.setModels(new ArrayList<>());
        }
        settings.getProviders().forEach(provider -> {
            if (!StringUtils.hasText(provider.getCode())) {
                provider.setCode(slug(provider.getName()));
            }
        });
    }

    private String slug(String input) {
        if (!StringUtils.hasText(input)) {
            return UUID.randomUUID().toString();
        }
        return input.trim().replaceAll("[^a-zA-Z0-9]", "-").toLowerCase(Locale.ROOT);
    }

    public boolean isAiEnabled() {
        return Boolean.TRUE.equals(getSettings().getEnabled());
    }

    public void ensureAiEnabled() {
        if (!isAiEnabled()) {
            throw new AiSafetyException("全站 AI 功能已由管理员关闭");
        }
    }

    public String resolveChatModel() {
        AiSettingsDTO settings = getSettings();
        ensureAiEnabled();
        String candidate = settings.getDefaultChatModel();
        if (!isModelEnabled(settings, candidate)) {
            candidate = settings.getFallbackChatModel();
        }
        if (!StringUtils.hasText(candidate)) {
            throw new AiSafetyException("未配置可用的对话模型，请联系超级管理员");
        }
        return candidate;
    }

    public Optional<String> resolveProviderByModel(String modelName) {
        if (!StringUtils.hasText(modelName)) {
            return Optional.empty();
        }
        return getSettings().getModels().stream()
                .filter(model -> modelName.equals(model.getName()))
                .map(AiSettingsDTO.Model::getProvider)
                .filter(StringUtils::hasText)
                .findFirst();
    }

    private boolean isModelEnabled(AiSettingsDTO settings, String modelName) {
        if (!StringUtils.hasText(modelName)) {
            return false;
        }
        return settings.getModels().stream()
                .anyMatch(model -> modelName.equals(model.getName()) && Boolean.TRUE.equals(model.getEnabled()));
    }

    public List<AiSettingsDTO.Provider> listProviders() {
        return getSettings().getProviders();
    }

    public synchronized void upsertProvider(AiSettingsDTO.Provider provider) {
        AiSettingsDTO settings = getSettings();
        provider.setCode(slug(provider.getCode() != null ? provider.getCode() : provider.getName()));
        List<AiSettingsDTO.Provider> providers = settings.getProviders().stream()
                .filter(item -> !Objects.equals(item.getCode(), provider.getCode()))
                .collect(Collectors.toCollection(ArrayList::new));
        providers.add(provider);
        settings.setProviders(providers);
        saveSettings(settings);
    }

    public synchronized void deleteProvider(String code) {
        AiSettingsDTO settings = getSettings();
        settings.setProviders(settings.getProviders().stream()
                .filter(provider -> !Objects.equals(provider.getCode(), code))
                .collect(Collectors.toCollection(ArrayList::new)));
        saveSettings(settings);
    }

    public List<AiSettingsDTO.Model> listModels() {
        return getSettings().getModels().stream()
                .sorted(Comparator.comparing(AiSettingsDTO.Model::getName))
                .collect(Collectors.toList());
    }

    public synchronized void upsertModel(AiSettingsDTO.Model model) {
        AiSettingsDTO settings = getSettings();
        List<AiSettingsDTO.Model> models = settings.getModels().stream()
                .filter(item -> !Objects.equals(item.getName(), model.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
        models.add(model);
        settings.setModels(models);
        saveSettings(settings);
    }

    public synchronized void deleteModel(String name) {
        AiSettingsDTO settings = getSettings();
        settings.setModels(settings.getModels().stream()
                .filter(model -> !Objects.equals(model.getName(), name))
                .collect(Collectors.toCollection(ArrayList::new)));
        saveSettings(settings);
    }

    public synchronized void changeModelStatus(String name, boolean enabled) {
        AiSettingsDTO settings = getSettings();
        settings.getModels().forEach(model -> {
            if (Objects.equals(model.getName(), name)) {
                model.setEnabled(enabled);
            }
        });
        saveSettings(settings);
    }

    public synchronized void updateDefaults(AiModelDefaultRequest request) {
        AiSettingsDTO settings = getSettings();
        settings.setDefaultChatModel(request.getDefaultChatModel());
        settings.setFallbackChatModel(request.getFallbackChatModel());
        settings.setDefaultRerankModel(request.getDefaultRerankModel());
        settings.setDefaultEmbeddingModel(request.getDefaultEmbeddingModel());
        saveSettings(settings);
    }

    public AiServiceStatusDTO createStatusSkeleton() {
        AiSettingsDTO settings = getSettings();
        AiServiceStatusDTO dto = new AiServiceStatusDTO();
        dto.setAiEnabled(Boolean.TRUE.equals(settings.getEnabled()));
        dto.setGlobalSwitch(Boolean.TRUE.equals(settings.getEnabled()));
        dto.setActiveChatModel(resolveChatModelSilently(settings));
        dto.setFallbackChatModel(settings.getFallbackChatModel());
        return dto;
    }

    private String resolveChatModelSilently(AiSettingsDTO settings) {
        String candidate = settings.getDefaultChatModel();
        if (!isModelEnabled(settings, candidate)) {
            candidate = settings.getFallbackChatModel();
        }
        return candidate;
    }

    public boolean validateAlert(AiSettingsDTO.AlertRule rule, AiModelStatDTO stat) {
        if (rule.getThreshold() == null) {
            return false;
        }
        if (rule.getMetric() == null) {
            return false;
        }
        double value = switch (rule.getMetric()) {
            case "errorRate" -> stat.getErrorRate();
            case "latency" -> stat.getAvgLatencyMs();
            default -> -1;
        };
        if (value < 0) {
            return false;
        }
        double threshold = rule.getThreshold();
        return switch (rule.getComparator()) {
            case "lte" -> value <= threshold;
            default -> value >= threshold;
        };
    }

    public synchronized void updateScenarioFallbacks(Map<String, String> fallbacks) {
        AiSettingsDTO settings = getSettings();
        settings.setScenarioFallbacks(fallbacks);
        saveSettings(settings);
    }
}
