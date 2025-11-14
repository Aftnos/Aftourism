package aftnos.aftourismserver.ai.dto;

import java.time.Instant;

/**
 * 前端展示使用的消息结构。
 */
public class AiMessageDTO {

    private String role;
    private String content;
    private Instant timestamp;

    public AiMessageDTO() {
    }

    public AiMessageDTO(String role, String content, Instant timestamp) {
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
