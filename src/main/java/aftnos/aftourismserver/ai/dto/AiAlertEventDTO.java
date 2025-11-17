package aftnos.aftourismserver.ai.dto;

import lombok.Data;

import java.time.Instant;

/**
 * 告警事件记录。
 */
@Data
public class AiAlertEventDTO {
    private String code;
    private String model;
    private String provider;
    private String metric;
    private double value;
    private double threshold;
    private String comparator;
    private String message;
    private Instant occurredAt;
}
