package aftnos.aftourismserver.ai.dto;

/**
 * 工具执行确认后的响应。
 */
public class AiToolConfirmationResponse {

    private AiPendingToolDTO pendingTool;
    private boolean success;
    private String message;

    public AiPendingToolDTO getPendingTool() {
        return pendingTool;
    }

    public void setPendingTool(AiPendingToolDTO pendingTool) {
        this.pendingTool = pendingTool;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
