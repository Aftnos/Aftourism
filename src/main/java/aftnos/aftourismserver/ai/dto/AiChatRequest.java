package aftnos.aftourismserver.ai.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * AI 模块的数据传输对象，用于封装向聊天服务发送的请求参数。
 */
public class AiChatRequest {

    /**
     * 对话 ID，用于标识一次聊天会话；为空时表示开启新的对话。
     */
    private String conversationId;

    /**
     * 用户发送的消息内容，不能为空。
     */
    @NotBlank(message = "提问内容不能为空")
    private String message;

    /**
     * 是否启用流式响应，开启后模型回复将分段返回。
     */
    private boolean stream;

    /**
     * 获取对话 ID。
     *
     * @return 对话 ID，可能为空
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * 设置对话 ID。
     *
     * @param conversationId 对话 ID
     */
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * 获取用户消息内容。
     *
     * @return 用户消息文本
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置用户消息内容。
     *
     * @param message 用户消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 判断是否启用流式响应。
     *
     * @return true 表示启用流式输出，false 表示等待完整响应
     */
    public boolean isStream() {
        return stream;
    }

    /**
     * 设置是否启用流式响应。
     *
     * @param stream 流式响应开关
     */
    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
