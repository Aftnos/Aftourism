package aftnos.aftourismserver.ai.dto;

import aftnos.aftourismserver.ai.tool.AiOperationGuide;

import java.time.Instant;
import java.util.Map;

/**
 * 待确认工具信息。
 */
public class AiPendingToolDTO {

    private String toolCallId;
    private String toolName;
    private String description;
    private String summary;
    private String status;
    private Instant createdAt;
    private String riskLevel;
    private Map<String, Object> params;
    private AiOperationGuide operationGuide;

    public String getToolCallId() {
        return toolCallId;
    }

    public void setToolCallId(String toolCallId) {
        this.toolCallId = toolCallId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public AiOperationGuide getOperationGuide() {
        return operationGuide;
    }

    public void setOperationGuide(AiOperationGuide operationGuide) {
        this.operationGuide = operationGuide;
    }
}
