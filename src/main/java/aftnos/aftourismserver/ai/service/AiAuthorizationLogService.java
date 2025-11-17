package aftnos.aftourismserver.ai.service;

import aftnos.aftourismserver.ai.dto.AiAuthorizationLogDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 敏感操作授权日志持久化。
 */
@Service
public class AiAuthorizationLogService {

    private static final String KEY = "ai:authorization:logs";
    private final RedisTemplate<String, Object> redisTemplate;

    public AiAuthorizationLogService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void record(AiAuthorizationLogDTO entry) {
        if (entry.getId() == null) {
            entry.setId(UUID.randomUUID().toString());
        }
        if (entry.getOccurredAt() == null) {
            entry.setOccurredAt(Instant.now());
        }
        redisTemplate.opsForList().leftPush(KEY, entry);
        redisTemplate.opsForList().trim(KEY, 0, 199);
    }

    public List<AiAuthorizationLogDTO> latest(int limit) {
        List<Object> range = redisTemplate.opsForList().range(KEY, 0, limit - 1);
        if (range == null) {
            return List.of();
        }
        return range.stream()
                .map(this::convert)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private AiAuthorizationLogDTO convert(Object value) {
        if (value instanceof AiAuthorizationLogDTO dto) {
            return dto;
        }
        if (value instanceof Map<?, ?> map) {
            AiAuthorizationLogDTO dto = new AiAuthorizationLogDTO();
            dto.setId((String) map.get("id"));
            dto.setUserId((String) map.get("userId"));
            dto.setRole((String) map.get("role"));
            dto.setConversationId((String) map.get("conversationId"));
            dto.setToolCallId((String) map.get("toolCallId"));
            dto.setToolName((String) map.get("toolName"));
            dto.setOperation((String) map.get("operation"));
            dto.setApproved(Boolean.TRUE.equals(map.get("approved")));
            dto.setComment((String) map.get("comment"));
            Object occurredAt = map.get("occurredAt");
            dto.setOccurredAt(occurredAt == null ? null : java.time.Instant.parse(occurredAt.toString()));
            Object params = map.get("params");
            if (params instanceof Map<?, ?> raw) {
                dto.setParams((Map<String, Object>) raw);
            }
            dto.setResult((String) map.get("result"));
            return dto;
        }
        return null;
    }
}
