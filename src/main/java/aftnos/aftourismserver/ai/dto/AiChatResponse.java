package aftnos.aftourismserver.ai.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 对话响应结构。
 */
public class AiChatResponse {

    private String conversationId;
    private boolean closed;
    private String closeReason;
    private AiStructuredReply structured;
    private List<AiMessageDTO> history = new ArrayList<>();
    private AiPendingToolDTO pendingTool;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public AiStructuredReply getStructured() {
        return structured;
    }

    public void setStructured(AiStructuredReply structured) {
        this.structured = structured;
    }

    public List<AiMessageDTO> getHistory() {
        return history;
    }

    public void setHistory(List<AiMessageDTO> history) {
        this.history = history;
    }

    public AiPendingToolDTO getPendingTool() {
        return pendingTool;
    }

    public void setPendingTool(AiPendingToolDTO pendingTool) {
        this.pendingTool = pendingTool;
    }
}
