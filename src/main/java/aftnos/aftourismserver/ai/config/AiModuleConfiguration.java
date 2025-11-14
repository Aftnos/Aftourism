package aftnos.aftourismserver.ai.config;

import aftnos.aftourismserver.ai.conversation.AiConversationRepository;
import aftnos.aftourismserver.ai.tool.AiServerTool;
import aftnos.aftourismserver.ai.tool.AiToolRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * AI 模块核心 Bean 配置。
 */
@Configuration
public class AiModuleConfiguration {

    @Bean
    public AiConversationRepository aiConversationRepository() {
        return new AiConversationRepository();
    }

    @Bean
    public AiToolRegistry aiToolRegistry(List<AiServerTool<?>> tools) {
        AiToolRegistry registry = new AiToolRegistry();
        tools.forEach(registry::register);
        return registry;
    }

    @Bean
    public ChatClient aiChatClient(ChatClient.Builder builder, List<Advisor> advisors) {
        return builder.defaultAdvisors(advisors).build();
    }
}
