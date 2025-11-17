package aftnos.aftourismserver.ai.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * 将工具调用参数渲染为易懂的授权说明。
 */
@Service
public class SensitiveOperationTemplateService {

    private final Map<String, TemplateRule> templates = new HashMap<>();
    private final ObjectMapper objectMapper;

    public SensitiveOperationTemplateService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        registerDefaults();
    }

    private void registerDefaults() {
        templates.put("AdminActivityApprovalTool", TemplateRule.of("活动审核", "将把指定活动调整为已审核状态", "L2",
                (guide, payload) -> {
                    guide.getParameters().put("活动ID", payload.getOrDefault("activityId", "未知"));
                    guide.getParameters().put("操作者备注", payload.getOrDefault("comment", "-"));
                }));
        templates.put("ModelToggleTool", TemplateRule.of("模型启停", "用于启用/停用后台注册的模型", "L3",
                (guide, payload) -> {
                    guide.getParameters().put("模型", payload.getOrDefault("model", "未知"));
                    guide.getParameters().put("目标状态", payload.getOrDefault("targetStatus", "未知"));
                }));
    }

    public AiOperationGuide render(String toolName, Object inputPayload, String summary) {
        Map<String, Object> map = convertPayload(inputPayload);
        TemplateRule rule = templates.get(toolName);
        AiOperationGuide guide = new AiOperationGuide();
        guide.setOperationType(toolName);
        guide.setDescription(summary);
        if (rule != null) {
            guide.setTitle(rule.title());
            guide.setRiskLevel(rule.riskLevel());
            rule.parameterFiller().accept(guide, map);
        } else {
            guide.setTitle("敏感操作授权");
            guide.setRiskLevel(inferRisk(summary));
            guide.getParameters().putAll(map);
        }
        return guide;
    }

    private Map<String, Object> convertPayload(Object inputPayload) {
        if (inputPayload instanceof Map<?, ?> raw) {
            Map<String, Object> map = new HashMap<>();
            raw.forEach((k, v) -> map.put(String.valueOf(k), v));
            return map;
        }
        if (inputPayload == null) {
            return new HashMap<>();
        }
        return objectMapper.convertValue(inputPayload, Map.class);
    }

    private String inferRisk(String summary) {
        String lower = Optional.ofNullable(summary).orElse("").toLowerCase();
        if (lower.contains("删除") || lower.contains("停用") || lower.contains("danger")) {
            return "L3";
        }
        if (lower.contains("修改") || lower.contains("写")) {
            return "L2";
        }
        return "L1";
    }

    private record TemplateRule(String title, String description, String riskLevel,
                                BiConsumer<AiOperationGuide, Map<String, Object>> parameterFiller) {
        static TemplateRule of(String title, String description, String riskLevel,
                               BiConsumer<AiOperationGuide, Map<String, Object>> parameterFiller) {
            return new TemplateRule(title, description, riskLevel, (guide, payload) -> {
                guide.setDescription(description);
                parameterFiller.accept(guide, payload);
            });
        }
    }
}
