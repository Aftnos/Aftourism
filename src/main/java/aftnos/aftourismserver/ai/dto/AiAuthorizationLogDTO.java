package aftnos.aftourismserver.ai.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

/**
 * 敏感操作授权日志。
 */
@Data
public class AiAuthorizationLogDTO {
    private String id;
    private String userId;
    private String role;
    private String conversationId;
    private String toolCallId;
    private String toolName;
    private String operation;
    private boolean approved;
    private String comment;
    private Instant occurredAt;
    private Map<String, Object> params;
    private String result;
}
