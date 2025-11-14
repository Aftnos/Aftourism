package aftnos.aftourismserver.ai.tool.impl;

import aftnos.aftourismserver.admin.service.ActivityService;
import aftnos.aftourismserver.ai.context.AiPrincipalProfile;
import aftnos.aftourismserver.ai.context.AiPrincipalType;
import aftnos.aftourismserver.ai.conversation.AiConversation;
import aftnos.aftourismserver.ai.tool.AiServerTool;
import aftnos.aftourismserver.ai.tool.AiToolCallContext;
import aftnos.aftourismserver.ai.tool.AiToolExecutionResult;
import aftnos.aftourismserver.ai.tool.AiToolScope;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.AdminPermission;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 管理员活动审批工具，需要人工确认后方可执行。
 */
@Component
public class AdminActivityApprovalTool implements AiServerTool<AdminActivityApprovalTool.ActivityDecisionInput> {

    private final ActivityService activityService;

    public AdminActivityApprovalTool(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public String name() {
        return "admin_activity_decision";
    }

    @Override
    public String description() {
        return "用于根据管理员的审批意见通过或驳回活动申请";
    }

    @Override
    public AiToolScope scope() {
        return AiToolScope.ADMIN;
    }

    @Override
    public Optional<AdminPermission> requiredPermission() {
        return Optional.of(AdminPermission.ACTIVITY_APPROVE);
    }

    @Override
    public Class<ActivityDecisionInput> inputType() {
        return ActivityDecisionInput.class;
    }

    @Override
    public String summarizeRequest(ActivityDecisionInput input) {
        return "活动审批请求，活动ID=" + input.activityId() +
                (input.approve() ? "，拟审批通过" : "，拟驳回，原因=" + input.reason());
    }

    @Override
    public AiToolExecutionResult execute(AiToolCallContext<ActivityDecisionInput> context) {
        ActivityDecisionInput input = context.rawArguments();
        AiConversation conversation = context.conversation();
        AiPrincipalProfile profile = conversation.getPrincipalProfile();
        if (profile.getType() != AiPrincipalType.ADMIN) {
            return new AiToolExecutionResult(false, "仅管理员可执行活动审批", Map.of());
        }
        try {
            if (input.approve()) {
                activityService.approve(input.activityId());
            } else {
                activityService.reject(input.activityId(), input.reason());
            }
            String message = input.approve() ? "已通过活动审批" : "已驳回活动";
            return new AiToolExecutionResult(true, message, Map.of(
                    "activityId", input.activityId(),
                    "approve", input.approve(),
                    "comment", context.comment()
            ));
        } catch (BusinessException ex) {
            return new AiToolExecutionResult(false, ex.getMessage(), Map.of());
        }
    }

    /**
     * 审批入参结构，配合 Spring AI 的结构化输出。
     */
    public record ActivityDecisionInput(Long activityId, boolean approve, String reason) {
    }
}
