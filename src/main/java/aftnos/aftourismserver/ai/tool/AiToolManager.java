package aftnos.aftourismserver.ai.tool;

import aftnos.aftourismserver.ai.context.AiAdvisorContext;
import aftnos.aftourismserver.ai.context.AiPrincipalProfile;
import aftnos.aftourismserver.ai.context.AiPrincipalType;
import aftnos.aftourismserver.ai.conversation.AiConversation;
import aftnos.aftourismserver.ai.conversation.AiPendingToolCall;
import aftnos.aftourismserver.ai.conversation.AiToolStatus;
import aftnos.aftourismserver.common.security.AdminPermission;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工具执行与确认流程的协调器。
 */
@Component
public class AiToolManager {

    private static final Logger log = LoggerFactory.getLogger(AiToolManager.class);

    private final AiToolRegistry registry;
    private final ObjectMapper objectMapper;
    public AiToolManager(AiToolRegistry registry,
                         ObjectMapper objectMapper) {
        this.registry = registry;
        this.objectMapper = objectMapper;
    }

    /**
     * 根据会话上下文组装可用的工具回调。
     */
    public List<FunctionCallback> buildCallbacks(AiAdvisorContext context) {
        AiPrincipalProfile profile = context.getPrincipalProfile();
        List<FunctionCallback> callbacks = new ArrayList<>();
        for (AiServerTool<?> tool : registry.listAll()) {
            if (!isToolVisible(tool, profile)) {
                continue;
            }
            callbacks.add(createCallback(tool));
        }
        return callbacks;
    }

    private boolean isToolVisible(AiServerTool<?> tool, AiPrincipalProfile profile) {
        if (tool.scope() == AiToolScope.USER) {
            return profile.getType() == AiPrincipalType.USER;
        }
        if (profile.getType() != AiPrincipalType.ADMIN) {
            return false;
        }
        Optional<AdminPermission> required = tool.requiredPermission();
        if (required.isEmpty()) {
            return true;
        }
        return profile.getAllowPermissions().stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet())
                .contains(required.get().asKey());
    }

    @SuppressWarnings("unchecked")
    private FunctionCallback createCallback(AiServerTool<?> tool) {
        return createTypedCallback((AiServerTool<Object>) tool);
    }

    private <T> FunctionCallback createTypedCallback(AiServerTool<T> tool) {
        return FunctionCallback.builder()
                .function(tool.name(), (T input, org.springframework.ai.chat.model.ToolContext toolContext) -> {
                    AiAdvisorContext advisorContext = aftnos.aftourismserver.ai.context.AiAdvisorContextHolder.get();
                    if (advisorContext == null) {
                        log.warn("未能获取 Advisor 上下文，拒绝执行工具: {}", tool.name());
                        return createPendingResponse("advisor_context_missing", "无法识别会话上下文，已拒绝工具调用");
                    }
                    AiConversation conversation = advisorContext.getConversation();
                    AiPrincipalProfile profile = advisorContext.getPrincipalProfile();
                    if (!isToolVisible(tool, profile)) {
                        log.warn("主体 {} 无权使用工具 {}", profile.getPrincipalId(), tool.name());
                        return createPendingResponse("permission_denied", "当前身份无权执行该操作");
                    }
                    AiPendingToolCall pending = registerPending(conversation, tool, input);
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("status", "PENDING");
                    payload.put("toolCallId", pending.getId());
                    payload.put("summary", pending.getSummary());
                    payload.put("requiresConfirmation", true);
                    payload.put("toolName", pending.getToolName());
                    try {
                        return objectMapper.writeValueAsString(payload);
                    } catch (Exception e) {
                        log.error("序列化工具响应失败", e);
                        return createPendingResponse("serialization_error", "系统内部错误，已记录日志");
                    }
                })
                .inputType(tool.inputType())
                .build();
    }

    private <T> AiPendingToolCall registerPending(AiConversation conversation,
                                                  AiServerTool<T> tool,
                                                  T input) {
        String summary = tool.summarizeRequest(input);
        AiPendingToolCall call = new AiPendingToolCall(tool.name(), tool.description(), summary, input, tool.scope());
        conversation.addPendingToolCall(call);
        conversation.appendMessage(new aftnos.aftourismserver.ai.conversation.AiMessageRecord(
                aftnos.aftourismserver.ai.conversation.AiMessageRole.TOOL,
                "工具请求等待人工确认: " + summary,
                java.time.Instant.now()));
        return call;
    }

    private String createPendingResponse(String code, String message) {
        try {
            Map<String, Object> payload = Map.of(
                    "status", code,
                    "message", message,
                    "requiresConfirmation", true);
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.error("生成工具响应失败", e);
            return "{\"status\":\"error\",\"message\":\"工具调用失败\"}";
        }
    }

    /**
     * 人工确认或拒绝工具调用。
     */
    @SuppressWarnings("unchecked")
    public AiToolExecutionResult confirmTool(AiConversation conversation,
                                             String callId,
                                             boolean approved,
                                             String comment) {
        AiPendingToolCall call = conversation.findPendingToolCall(callId)
                .orElseThrow(() -> new IllegalArgumentException("工具请求不存在或已处理"));
        if (!approved) {
            call.setStatus(AiToolStatus.REJECTED);
            call.setLastMessage(comment);
            return new AiToolExecutionResult(false, comment == null ? "已拒绝执行该操作" : comment, Map.of());
        }
        call.setStatus(AiToolStatus.EXECUTING);
        AiServerTool<Object> tool = (AiServerTool<Object>) registry.find(call.getToolName())
                .orElseThrow(() -> new IllegalStateException("工具定义缺失"));
        Object input = call.getInputPayload();
        AiToolCallContext<Object> context = new AiToolCallContext<>(conversation, conversation.getPrincipalProfile(), input, comment);
        AiToolExecutionResult result = tool.execute(context);
        call.setStatus(AiToolStatus.COMPLETED);
        call.setLastMessage(result.getMessage());
        conversation.appendMessage(new aftnos.aftourismserver.ai.conversation.AiMessageRecord(
                aftnos.aftourismserver.ai.conversation.AiMessageRole.TOOL,
                "工具执行完成: " + result.getMessage(),
                java.time.Instant.now()));
        return result;
    }
}
