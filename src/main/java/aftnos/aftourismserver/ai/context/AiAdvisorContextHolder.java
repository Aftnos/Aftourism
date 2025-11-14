package aftnos.aftourismserver.ai.context;

/**
 * 使用 ThreadLocal 保存 Advisor 需要的上下文信息，确保同一线程内共享。
 */
public final class AiAdvisorContextHolder {

    private static final ThreadLocal<AiAdvisorContext> CONTEXT = new ThreadLocal<>();

    private AiAdvisorContextHolder() {
    }

    public static void set(AiAdvisorContext context) {
        if (context == null) {
            CONTEXT.remove();
        } else {
            CONTEXT.set(context);
        }
    }

    public static AiAdvisorContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
