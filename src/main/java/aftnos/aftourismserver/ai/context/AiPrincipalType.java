package aftnos.aftourismserver.ai.context;

/**
 * AI 会话中识别的主体类型枚举。
 */
public enum AiPrincipalType {
    /** 普通门户用户，仅可申请/查询。 */
    USER,
    /** 普通管理员，可执行审批/编辑类操作。 */
    ADMIN
}
