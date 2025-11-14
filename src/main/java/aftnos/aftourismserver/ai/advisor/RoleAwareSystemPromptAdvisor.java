package aftnos.aftourismserver.ai.advisor;

import aftnos.aftourismserver.ai.context.AiAdvisorContext;
import aftnos.aftourismserver.ai.context.AiAdvisorContextHolder;
import aftnos.aftourismserver.ai.prompt.AiPromptComposer;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据主体动态注入系统提示词与工具上下文。
 */
@Component
public class RoleAwareSystemPromptAdvisor implements CallAroundAdvisor {

    private final AiPromptComposer promptComposer;
    public RoleAwareSystemPromptAdvisor(AiPromptComposer promptComposer) {
        this.promptComposer = promptComposer;
    }

    @Override
    public String getName() {
        return "roleAwareSystemPromptAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest request, CallAroundAdvisorChain chain) {
        AiAdvisorContext context = AiAdvisorContextHolder.get();
        if (context == null) {
            return chain.nextAroundCall(request);
        }
        String systemPrompt = promptComposer.composeSystemPrompt(context);
        var builder = AdvisedRequest.from(request)
                .systemText(systemPrompt)
                .functionCallbacks(context.getFunctionCallbacks());
        Map<String, Object> toolContext = new HashMap<>(context.getToolContext());
        toolContext.put("conversationId", context.getConversation().getConversationId());
        var nextRequest = builder.toolContext(toolContext).build();
        return chain.nextAroundCall(nextRequest);
    }
}
