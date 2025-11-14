package aftnos.aftourismserver.ai.conversation;

import aftnos.aftourismserver.ai.context.AiPrincipalProfile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AI 会话状态，保存历史消息与工具调用信息。
 */
public class AiConversation {

    private final String conversationId;
    private final AiPrincipalProfile principalProfile;
    private final List<AiMessageRecord> messages = new CopyOnWriteArrayList<>();
    private final Map<String, AiPendingToolCall> pendingToolCalls = new ConcurrentHashMap<>();
    private volatile boolean closed;
    private volatile String closeReason;
    private volatile Instant closedAt;

    public AiConversation(String conversationId, AiPrincipalProfile principalProfile) {
        this.conversationId = Objects.requireNonNull(conversationId, "会话ID不能为空");
        this.principalProfile = Objects.requireNonNull(principalProfile, "主体信息不能为空");
    }

    public String getConversationId() {
        return conversationId;
    }

    public AiPrincipalProfile getPrincipalProfile() {
        return principalProfile;
    }

    public void appendMessage(AiMessageRecord record) {
        if (record != null) {
            messages.add(record);
        }
    }

    public List<AiMessageRecord> getMessages() {
        return Collections.unmodifiableList(new ArrayList<>(messages));
    }

    public AiPendingToolCall addPendingToolCall(AiPendingToolCall call) {
        pendingToolCalls.put(call.getId(), call);
        return call;
    }

    public Optional<AiPendingToolCall> findPendingToolCall(String callId) {
        if (callId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(pendingToolCalls.get(callId));
    }

    public Collection<AiPendingToolCall> getPendingToolCalls() {
        return Collections.unmodifiableCollection(pendingToolCalls.values());
    }

    public void removePendingTool(String callId) {
        if (callId != null) {
            pendingToolCalls.remove(callId);
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public Optional<String> getCloseReason() {
        return Optional.ofNullable(closeReason);
    }

    public Optional<Instant> getClosedAt() {
        return Optional.ofNullable(closedAt);
    }

    public void close(String reason) {
        this.closed = true;
        this.closeReason = reason;
        this.closedAt = Instant.now();
    }

    public void reopenIfSafe() {
        this.closed = false;
        this.closeReason = null;
        this.closedAt = null;
    }
}
