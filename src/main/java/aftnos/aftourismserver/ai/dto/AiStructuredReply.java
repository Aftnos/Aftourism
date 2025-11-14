package aftnos.aftourismserver.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 约定的结构化回答格式，方便进行 JSON 解析。
 */
public class AiStructuredReply {

    private String reply;
    private List<AiActionSuggestion> actions = new ArrayList<>();
    @JsonProperty("needsConfirmation")
    private Boolean needsConfirmation;
    @JsonProperty("pendingToolId")
    private String pendingToolId;
    @JsonProperty("securityNotice")
    private String securityNotice;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<AiActionSuggestion> getActions() {
        return actions;
    }

    public void setActions(List<AiActionSuggestion> actions) {
        this.actions = actions;
    }

    public Boolean getNeedsConfirmation() {
        return needsConfirmation;
    }

    public void setNeedsConfirmation(Boolean needsConfirmation) {
        this.needsConfirmation = needsConfirmation;
    }

    public String getPendingToolId() {
        return pendingToolId;
    }

    public void setPendingToolId(String pendingToolId) {
        this.pendingToolId = pendingToolId;
    }

    public String getSecurityNotice() {
        return securityNotice;
    }

    public void setSecurityNotice(String securityNotice) {
        this.securityNotice = securityNotice;
    }
}
