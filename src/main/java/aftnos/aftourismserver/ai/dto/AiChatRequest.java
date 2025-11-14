package aftnos.aftourismserver.ai.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户发送消息的请求结构。
 */
public class AiChatRequest {

    private String conversationId;
    @NotBlank(message = "提问内容不能为空")
    private String message;
    private boolean stream;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
