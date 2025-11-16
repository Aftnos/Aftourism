package aftnos.aftourismserver.ai.dto;

import jakarta.validation.constraints.NotBlank;

// AI聊天请求数据传输对象
// 用于封装用户向AI发送聊天消息的请求参数
public class AiChatRequest {

    // 对话ID，用于标识一次对话会话
    // 如果为空，则表示开启新的对话
    private String conversationId;
    
    // 用户发送的消息内容
    // 此字段不能为空
    @NotBlank(message = "提问内容不能为空")
    private String message;
    
    // 是否启用流式响应
    // true: 启用流式响应，消息将逐步返回
    // false: 不启用流式响应，等待完整响应后一次性返回
    private boolean stream;

    // 获取对话ID
    //
    // @return 对话ID字符串，可能为null
    public String getConversationId() {
        return conversationId;
    }

    // 设置对话ID
    //
    // @param conversationId 对话ID
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    // 获取用户消息内容
    //
    // @return 用户消息内容
    public String getMessage() {
        return message;
    }

    // 设置用户消息内容
    //
    // @param message 用户消息内容
    public void setMessage(String message) {
        this.message = message;
    }

    // 判断是否启用流式响应
    //
    // @return true: 启用流式响应, false: 不启用流式响应
    public boolean isStream() {
        return stream;
    }

    // 设置是否启用流式响应
    //
    // @param stream 流式响应开关
    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
