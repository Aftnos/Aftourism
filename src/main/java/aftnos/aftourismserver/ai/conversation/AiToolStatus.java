package aftnos.aftourismserver.ai.conversation;

/**
 * 工具调用的状态。
 */
public enum AiToolStatus {
    /** 等待人工确认 */
    AWAITING_CONFIRMATION,
    /** 拒绝执行 */
    REJECTED,
    /** 正在执行 */
    EXECUTING,
    /** 已执行完成 */
    COMPLETED
}
