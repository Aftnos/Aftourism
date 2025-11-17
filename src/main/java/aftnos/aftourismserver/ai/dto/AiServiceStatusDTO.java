package aftnos.aftourismserver.ai.dto;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * AI 服务运行状态面板数据。
 */
@Data
public class AiServiceStatusDTO {

    private boolean aiEnabled;
    private boolean globalSwitch;
    private String activeChatModel;
    private String fallbackChatModel;
    private List<ServiceHealth> providers = new ArrayList<>();
    private List<ErrorEntry> recentErrors = new ArrayList<>();

    @Data
    public static class ServiceHealth {
        private String provider;
        private String endpoint;
        private String status;
        private Long latencyMs;
        private Instant checkedAt;
    }

    @Data
    public static class ErrorEntry {
        private String provider;
        private String model;
        private String message;
        private Instant occurredAt;
    }
}
