package aftnos.aftourismserver.ai.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 对话响应结构。
 */
public class AiChatResponse {

    // 对话ID，用于标识唯一的对话会话
    private String conversationId;
    // 对话是否已关闭的标志
    private boolean closed;
    // 关闭原因描述
    private String closeReason;
    // 结构化回复内容
    private AiStructuredReply structured;
    // 对话历史记录列表
    private List<AiMessageDTO> history = new ArrayList<>();
    // 待处理的工具信息
    private AiPendingToolDTO pendingTool;

    // 获取对话ID
    public String getConversationId() {
        return conversationId;
    }

    // 设置对话ID
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    // 检查对话是否已关闭
    public boolean isClosed() {
        return closed;
    }

    // 设置对话关闭状态
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    // 获取对话关闭原因
    public String getCloseReason() {
        return closeReason;
    }

    // 设置对话关闭原因
    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    // 获取结构化回复内容
    public AiStructuredReply getStructured() {
        return structured;
    }

    // 设置结构化回复内容
    public void setStructured(AiStructuredReply structured) {
        this.structured = structured;
    }

    // 获取对话历史记录列表
    public List<AiMessageDTO> getHistory() {
        return history;
    }

    // 设置对话历史记录列表
    public void setHistory(List<AiMessageDTO> history) {
        this.history = history;
    }

    // 获取待处理的工具信息
    public AiPendingToolDTO getPendingTool() {
        return pendingTool;
    }

    // 设置待处理的工具信息
    public void setPendingTool(AiPendingToolDTO pendingTool) {
        this.pendingTool = pendingTool;
    }
}