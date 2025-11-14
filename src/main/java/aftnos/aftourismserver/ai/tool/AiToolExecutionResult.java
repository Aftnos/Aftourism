package aftnos.aftourismserver.ai.tool;

import java.time.Instant;
import java.util.Map;

/**
 * 工具执行结果封装。
 */
public class AiToolExecutionResult {

    private final boolean success;
    private final String message;
    private final Map<String, Object> data;
    private final Instant executedAt;

    public AiToolExecutionResult(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.executedAt = Instant.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }
}
