package aftnos.aftourismserver.ai.advisor;

import aftnos.aftourismserver.ai.context.AiAdvisorContext;
import aftnos.aftourismserver.ai.context.AiAdvisorContextHolder;
import aftnos.aftourismserver.ai.safety.AiSafetyException;
import aftnos.aftourismserver.ai.safety.AiSafetyService;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在调用大模型前执行输入安全校验，若失败直接返回提示并关闭会话。
 */
@Component
public class AiSafetyAdvisor implements CallAroundAdvisor {

    private final AiSafetyService safetyService;

    public AiSafetyAdvisor(AiSafetyService safetyService) {
        this.safetyService = safetyService;
    }

    @Override
    public String getName() {
        return "aiSafetyAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest request, CallAroundAdvisorChain chain) {
        AiAdvisorContext context = AiAdvisorContextHolder.get();
        if (context == null) {
            return chain.nextAroundCall(request);
        }
        try {
            safetyService.ensureSafe(request.userText(), context.getPrincipalProfile());
        } catch (AiSafetyException ex) {
            context.getConversation().close(ex.getMessage());
            AssistantMessage message = new AssistantMessage("对不起，" + ex.getMessage());
            ChatResponse response = ChatResponse.builder()
                    .generations(List.of(new Generation(message)))
                    .build();
            return AdvisedResponse.builder().response(response).build();
        }
        return chain.nextAroundCall(request);
    }
}
