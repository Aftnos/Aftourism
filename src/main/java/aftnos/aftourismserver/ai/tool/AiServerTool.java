package aftnos.aftourismserver.ai.tool;

import aftnos.aftourismserver.common.security.AdminPermission;

import java.util.Optional;

/**
 * AI 可调用的后端工具接口。
 * @param <T> 工具入参类型
 */
public interface AiServerTool<T> {

    /** 工具唯一名称，需与 LLM 配置保持一致。 */
    String name();

    /** 工具用途说明，供提示词展示。 */
    String description();

    /** 工具作用域，用于限制访问主体。 */
    AiToolScope scope();

    /**
     * 管理员工具所需的权限点。
     */
    default Optional<AdminPermission> requiredPermission() {
        return Optional.empty();
    }

    /** 入参对应的 Java 类型。 */
    Class<T> inputType();

    /**
     * 将模型生成的入参转为可读摘要，便于展示确认。
     */
    String summarizeRequest(T input);

    /**
     * 确认后执行工具逻辑。
     */
    AiToolExecutionResult execute(AiToolCallContext<T> context);
}
