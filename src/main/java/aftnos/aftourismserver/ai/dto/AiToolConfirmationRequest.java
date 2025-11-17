package aftnos.aftourismserver.ai.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 人工确认工具调用的请求。
 */
public class AiToolConfirmationRequest {

    @NotBlank(message = "工具调用编号不能为空")
    private String toolCallId;
    /**
     * 是否同意执行，默认视为 true，便于前端仅通过“同意”按钮授权。
     */
    private Boolean approved;
    private String comment;

    public String getToolCallId() {
        return toolCallId;
    }

    public void setToolCallId(String toolCallId) {
        this.toolCallId = toolCallId;
    }

    public boolean isApproved() {
        return approved == null || approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
