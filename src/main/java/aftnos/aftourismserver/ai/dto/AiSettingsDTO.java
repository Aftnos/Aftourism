package aftnos.aftourismserver.ai.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 管理端统一配置模型，持久化在 Redis 中，供运行时动态读取。
 */
@Data
public class AiSettingsDTO {

    /**
     * 全局开关，关闭后所有会话入口均会拒绝服务。
     */
    private Boolean enabled = true;

    /** 默认对话模型 */
    private String defaultChatModel;

    /** 默认重排序模型 */
    private String defaultRerankModel;

    /** 默认向量嵌入模型 */
    private String defaultEmbeddingModel;

    /** 主模型不可用时的备用模型 */
    private String fallbackChatModel;

    /** 各业务场景定制化备份模型 */
    private Map<String, String> scenarioFallbacks = new HashMap<>();

    /** 接入的模型提供方 */
    private List<Provider> providers = new ArrayList<>();

    /** 模型清单 */
    private List<Model> models = new ArrayList<>();

    /** 告警规则配置 */
    private List<AlertRule> alertRules = new ArrayList<>();

    @Data
    public static class Provider {
        /** 服务商标识 */
        private String code;
        /** 展示名称 */
        private String name;
        private String apiKey;
        private Integer timeoutMs;
        private String endpoint;
        private Boolean enabled = true;
    }

    @Data
    public static class Model {
        private String name;
        private String provider;
        private AiModelType type = AiModelType.CHAT;
        private Boolean enabled = true;
        private Boolean defaultFallback = false;
        private String description;
    }

    @Data
    public static class AlertRule {
        /** 规则唯一标识 */
        private String code;
        /** 对应指标，例如 errorRate */
        private String metric;
        /** 阈值 */
        private Double threshold;
        /** 比较方式：gte、lte */
        private String comparator = "gte";
        /** 针对的模型，可为空表示全局 */
        private String targetModel;
        /** 告警说明 */
        private String message;
    }
}