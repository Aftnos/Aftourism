package aftnos.aftourismserver.ai.dto;

import lombok.Data;

/**
 * 设置默认模型请求体。
 */
@Data
public class AiModelDefaultRequest {
    private String defaultChatModel;
    private String fallbackChatModel;
    private String defaultRerankModel;
    private String defaultEmbeddingModel;
}
