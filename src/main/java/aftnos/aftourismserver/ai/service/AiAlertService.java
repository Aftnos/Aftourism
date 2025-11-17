package aftnos.aftourismserver.ai.service;

import aftnos.aftourismserver.ai.dto.AiAlertEventDTO;
import aftnos.aftourismserver.ai.dto.AiModelStatDTO;
import aftnos.aftourismserver.ai.dto.AiSettingsDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * 简易的异常告警事件记录器。
 */
@Service
public class AiAlertService {

    private final ConcurrentLinkedDeque<AiAlertEventDTO> events = new ConcurrentLinkedDeque<>();
    private final AiSettingsService settingsService;

    public AiAlertService(AiSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void evaluate(String model, String provider, AiModelStatDTO stat) {
        AiSettingsDTO settings = settingsService.getSettings();
        if (settings.getAlertRules() == null || settings.getAlertRules().isEmpty()) {
            return;
        }
        if (stat.getTotal() < 3) {
            return;
        }
        for (AiSettingsDTO.AlertRule rule : settings.getAlertRules()) {
            if (StringUtils.hasText(rule.getTargetModel()) && !rule.getTargetModel().equals(model)) {
                continue;
            }
            if (settingsService.validateAlert(rule, stat)) {
                AiAlertEventDTO event = new AiAlertEventDTO();
                event.setCode(rule.getCode());
                event.setModel(model);
                event.setProvider(provider);
                event.setMetric(rule.getMetric());
                event.setThreshold(rule.getThreshold());
                event.setComparator(rule.getComparator());
                event.setMessage(rule.getMessage());
                double value = switch (rule.getMetric()) {
                    case "errorRate" -> stat.getErrorRate();
                    case "latency" -> stat.getAvgLatencyMs();
                    default -> 0D;
                };
                event.setValue(value);
                event.setOccurredAt(Instant.now());
                events.addFirst(event);
                while (events.size() > 100) {
                    events.removeLast();
                }
            }
        }
    }

    public List<AiAlertEventDTO> listEvents() {
        return events.stream().collect(Collectors.toList());
    }
}
