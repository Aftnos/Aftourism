package aftnos.aftourismserver.ai.conversation;

import aftnos.aftourismserver.ai.context.AiPrincipalProfile;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的内存会话仓储，生产环境可替换为数据库/Redis。
 */
public class AiConversationRepository {

    private final Map<String, AiConversation> storage = new ConcurrentHashMap<>();

    public AiConversation create(AiPrincipalProfile profile) {
        String id = UUID.randomUUID().toString();
        AiConversation conversation = new AiConversation(id, profile);
        storage.put(id, conversation);
        return conversation;
    }

    public AiConversation save(AiConversation conversation) {
        storage.put(conversation.getConversationId(), conversation);
        return conversation;
    }

    public Optional<AiConversation> findById(String conversationId) {
        if (conversationId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(conversationId));
    }
}
