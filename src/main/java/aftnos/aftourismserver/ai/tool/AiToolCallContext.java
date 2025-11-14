package aftnos.aftourismserver.ai.tool;

import aftnos.aftourismserver.ai.context.AiPrincipalProfile;
import aftnos.aftourismserver.ai.conversation.AiConversation;

/**
 * 工具执行时可用的上下文信息。
 * @param conversation 当前会话
 * @param principal 当前登录主体
 * @param rawArguments 原始入参对象
 * @param comment 审批备注/用户确认信息
 */
public record AiToolCallContext<T>(AiConversation conversation,
                                   AiPrincipalProfile principal,
                                   T rawArguments,
                                   String comment) {
}
