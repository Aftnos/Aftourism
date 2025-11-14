package aftnos.aftourismserver.ai.context;

import aftnos.aftourismserver.ai.conversation.AiConversation;
import org.springframework.ai.model.function.FunctionCallback;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Advisor 执行链共享的上下文信息。
 */
public class AiAdvisorContext {

    private final AiConversation conversation;
    private final AiPrincipalProfile principalProfile;
    private final List<FunctionCallback> functionCallbacks;
    private final Map<String, Object> toolContext;

    public AiAdvisorContext(AiConversation conversation,
                            AiPrincipalProfile principalProfile,
                            List<FunctionCallback> functionCallbacks,
                            Map<String, Object> toolContext) {
        this.conversation = Objects.requireNonNull(conversation, "会话信息不能为空");
        this.principalProfile = Objects.requireNonNull(principalProfile, "主体信息不能为空");
        this.functionCallbacks = functionCallbacks == null
                ? List.of()
                : List.copyOf(functionCallbacks);
        this.toolContext = toolContext == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(toolContext);
    }

    public AiConversation getConversation() {
        return conversation;
    }

    public AiPrincipalProfile getPrincipalProfile() {
        return principalProfile;
    }

    public List<FunctionCallback> getFunctionCallbacks() {
        return functionCallbacks;
    }

    public Map<String, Object> getToolContext() {
        return toolContext;
    }
}
