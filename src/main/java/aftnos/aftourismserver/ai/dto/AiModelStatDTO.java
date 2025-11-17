package aftnos.aftourismserver.ai.dto;

import lombok.Data;

/**
 * 模型使用统计。
 */
@Data
public class AiModelStatDTO {
    private String model;
    private String provider;
    private long total;
    private long success;
    private long failure;
    private double avgLatencyMs;
    private double errorRate;
}
