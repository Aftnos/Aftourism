package aftnos.aftourismserver.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 约定的结构化回答格式，方便进行 JSON 解析。
 */
public class AiStructuredReply {

    // AI回复的主要文本内容
    private String reply;
    // 建议执行的动作列表
    private List<AiActionSuggestion> actions = new ArrayList<>();
    // 是否需要用户确认
    @JsonProperty("needsConfirmation")
    private Boolean needsConfirmation;
    // 待处理工具的ID
    @JsonProperty("pendingToolId")
    private String pendingToolId;
    // 安全提醒信息
    @JsonProperty("securityNotice")
    private String securityNotice;

    /**
     * 获取AI回复的主要文本内容
     * @return 回复文本
     */
    public String getReply() {
        return reply;
    }

    /**
     * 设置AI回复的主要文本内容
     * @param reply 回复文本
     */
    public void setReply(String reply) {
        this.reply = reply;
    }

    /**
     * 获取建议执行的动作列表
     * @return 动作建议列表
     */
    public List<AiActionSuggestion> getActions() {
        return actions;
    }

    /**
     * 设置建议执行的动作列表
     * @param actions 动作建议列表
     */
    public void setActions(List<AiActionSuggestion> actions) {
        this.actions = actions;
    }

    /**
     * 获取是否需要用户确认的标志
     * @return 需要确认标志
     */
    public Boolean getNeedsConfirmation() {
        return needsConfirmation;
    }

    /**
     * 设置是否需要用户确认的标志
     * @param needsConfirmation 需要确认标志
     */
    public void setNeedsConfirmation(Boolean needsConfirmation) {
        this.needsConfirmation = needsConfirmation;
    }

    /**
     * 获取待处理工具的ID
     * @return 工具ID
     */
    public String getPendingToolId() {
        return pendingToolId;
    }

    /**
     * 设置待处理工具的ID
     * @param pendingToolId 工具ID
     */
    public void setPendingToolId(String pendingToolId) {
        this.pendingToolId = pendingToolId;
    }

    /**
     * 获取安全提醒信息
     * @return 安全提醒文本
     */
    public String getSecurityNotice() {
        return securityNotice;
    }

    /**
     * 设置安全提醒信息
     * @param securityNotice 安全提醒文本
     */
    public void setSecurityNotice(String securityNotice) {
        this.securityNotice = securityNotice;
    }
}