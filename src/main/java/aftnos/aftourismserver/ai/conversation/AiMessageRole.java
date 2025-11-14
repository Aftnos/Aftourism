package aftnos.aftourismserver.ai.conversation;

/**
 * 会话消息的角色类型。
 */
public enum AiMessageRole {
    /** 来自终端用户的输入。 */
    USER,
    /** 模型生成的响应。 */
    ASSISTANT,
    /** 工具执行过程中的系统回馈。 */
    TOOL
}
