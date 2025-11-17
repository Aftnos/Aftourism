package aftnos.aftourismserver.ai.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用于在前端展示的敏感操作说明。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiOperationGuide {
    private String title;
    private String description;
    private String operationType;
    private String riskLevel;
    private Map<String, Object> parameters = new LinkedHashMap<>();
}
