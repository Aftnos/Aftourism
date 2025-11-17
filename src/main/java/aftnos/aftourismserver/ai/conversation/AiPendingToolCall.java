package aftnos.aftourismserver.ai.conversation;

import aftnos.aftourismserver.ai.tool.AiOperationGuide;
import aftnos.aftourismserver.ai.tool.AiToolScope;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * 待人工确认的工具调用。
 */
public class AiPendingToolCall {

    private final String id;
    private final String toolName;
    private final String toolDescription;
    private final String summary;
    private final Object inputPayload;
    private final AiToolScope scope;
    private final Instant createdAt;
    private AiToolStatus status;
    private String lastMessage;
    private final AiOperationGuide operationGuide;
    private final LinkedHashMap<String, Object> parameterSnapshot;

    public AiPendingToolCall(String toolName,
                              String toolDescription,
                              String summary,
                              Object inputPayload,
                              AiToolScope scope,
                              AiOperationGuide guide,
                              LinkedHashMap<String, Object> parameterSnapshot) {
        this.id = UUID.randomUUID().toString();
        this.toolName = Objects.requireNonNull(toolName, "工具名称不能为空");
        this.toolDescription = toolDescription == null ? "" : toolDescription;
        this.summary = summary == null ? "" : summary;
        this.inputPayload = inputPayload;
        this.scope = Objects.requireNonNull(scope, "工具作用域不能为空");
        this.createdAt = Instant.now();
        this.status = AiToolStatus.AWAITING_CONFIRMATION;
        this.operationGuide = guide;
        this.parameterSnapshot = parameterSnapshot == null ? new LinkedHashMap<>() : parameterSnapshot;
    }

    public String getId() {
        return id;
    }

    public String getToolName() {
        return toolName;
    }

    public String getToolDescription() {
        return toolDescription;
    }

    public String getSummary() {
        return summary;
    }

    public Object getInputPayload() {
        return inputPayload;
    }

    public AiToolScope getScope() {
        return scope;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AiToolStatus getStatus() {
        return status;
    }

    public void setStatus(AiToolStatus status) {
        this.status = Objects.requireNonNull(status, "状态不能为空");
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public AiOperationGuide getOperationGuide() {
        return operationGuide;
    }

    public LinkedHashMap<String, Object> getParameterSnapshot() {
        return new LinkedHashMap<>(parameterSnapshot);
    }
}
