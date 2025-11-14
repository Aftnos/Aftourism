package aftnos.aftourismserver.ai.conversation;

import java.time.Instant;
import java.util.Objects;

/**
 * AI 会话中的单条消息记录。
 */
public class AiMessageRecord {

    private final AiMessageRole role;
    private final String content;
    private final Instant timestamp;

    public AiMessageRecord(AiMessageRole role, String content, Instant timestamp) {
        this.role = Objects.requireNonNull(role, "消息角色不能为空");
        this.content = content == null ? "" : content;
        this.timestamp = timestamp == null ? Instant.now() : timestamp;
    }

    public AiMessageRole getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
