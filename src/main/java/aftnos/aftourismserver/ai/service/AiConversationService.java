package aftnos.aftourismserver.ai.service;

import aftnos.aftourismserver.ai.context.AiAdvisorContext;
import aftnos.aftourismserver.ai.context.AiAdvisorContextHolder;
import aftnos.aftourismserver.ai.context.AiPrincipalProfile;
import aftnos.aftourismserver.ai.context.AiPrincipalType;
import aftnos.aftourismserver.ai.conversation.AiConversation;
import aftnos.aftourismserver.ai.conversation.AiConversationRepository;
import aftnos.aftourismserver.ai.conversation.AiMessageRecord;
import aftnos.aftourismserver.ai.conversation.AiMessageRole;
import aftnos.aftourismserver.ai.conversation.AiPendingToolCall;
import aftnos.aftourismserver.ai.conversation.AiToolStatus;
import aftnos.aftourismserver.ai.dto.*;
import aftnos.aftourismserver.ai.safety.AiSafetyException;
import aftnos.aftourismserver.ai.safety.AiSafetyService;
import aftnos.aftourismserver.ai.tool.AiToolExecutionResult;
import aftnos.aftourismserver.ai.tool.AiToolManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import aftnos.aftourismserver.common.security.PortalUserPrincipal;
import aftnos.aftourismserver.common.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 核心会话服务，负责对接 Spring AI 并结合安全/工具逻辑。
 */
@Service
public class AiConversationService {

    private static final Logger log = LoggerFactory.getLogger(AiConversationService.class);

    private final ChatClient chatClient;
    private final AiConversationRepository conversationRepository;
    private final AiToolManager toolManager;
    private final AiSafetyService safetyService;
    private final AiSettingsService settingsService;
    private final AiUsageTracker usageTracker;
    private final AiAuthorizationLogService authorizationLogService;
    private final ObjectMapper objectMapper;

    public AiConversationService(ChatClient chatClient,
                                 AiConversationRepository conversationRepository,
                                 AiToolManager toolManager,
                                 AiSafetyService safetyService,
                                 AiSettingsService settingsService,
                                 AiUsageTracker usageTracker,
                                 AiAuthorizationLogService authorizationLogService,
                                 ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.conversationRepository = conversationRepository;
        this.toolManager = toolManager;
        this.safetyService = safetyService;
        this.settingsService = settingsService;
        this.usageTracker = usageTracker;
        this.authorizationLogService = authorizationLogService;
        this.objectMapper = objectMapper;
    }

    /**
     * 处理用户输入，返回结构化响应。
     */
    public AiChatResponse chat(AiChatRequest request) {
        return processChat(request, false, null);
    }

    /**
     * 流式响应接口。
     */
    public ResponseBodyEmitter chatStream(AiChatRequest request) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(0L);
        CompletableFuture.runAsync(() -> {
            try {
                sendEvent(emitter, "start", Map.of("conversationId", request.getConversationId()));
                AiChatResponse response = processChat(request, true, chunk -> sendEventSilently(emitter, "chunk", Map.of("content", chunk)));
                sendEvent(emitter, "end", response);
                emitter.complete();
            } catch (Exception ex) {
                sendEventSilently(emitter, "error", Map.of("message", ex.getMessage()));
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    private AiChatResponse processChat(AiChatRequest request, boolean streaming, Consumer<String> chunkConsumer) {
        String modelName = settingsService.resolveChatModel();
        String providerName = settingsService.resolveProviderByModel(modelName).orElse(null);
        AiPrincipalProfile profile = resolvePrincipal();
        AiConversation conversation = resolveConversation(request.getConversationId(), profile);
        AiChatResponse closedResponse = ensureConversationOpen(conversation);
        if (closedResponse != null) {
            closedResponse.setModelName(modelName);
            closedResponse.setProviderName(providerName);
            closedResponse.setStreaming(streaming);
            return closedResponse;
        }
        long start = System.currentTimeMillis();
        try {
            AiChatResponse response = executeCall(request, profile, conversation, chunkConsumer);
            response.setModelName(modelName);
            response.setProviderName(providerName);
            response.setStreaming(streaming);
            usageTracker.recordSuccess(modelName, providerName, System.currentTimeMillis() - start);
            return response;
        } catch (RuntimeException ex) {
            usageTracker.recordFailure(modelName, providerName, System.currentTimeMillis() - start, ex.getMessage());
            throw ex;
        }
    }

    private AiChatResponse ensureConversationOpen(AiConversation conversation) {
        if (!conversation.isClosed()) {
            return null;
        }
        AiChatResponse response = new AiChatResponse();
        response.setConversationId(conversation.getConversationId());
        response.setClosed(true);
        response.setCloseReason(conversation.getCloseReason().orElse("会话已结束，请重新发起"));
        return response;
    }

    private AiChatResponse executeCall(AiChatRequest request,
                                       AiPrincipalProfile profile,
                                       AiConversation conversation,
                                       Consumer<String> chunkConsumer) {
        safetyService.ensureSafe(request.getMessage(), profile);
        conversation.appendMessage(new AiMessageRecord(AiMessageRole.USER, request.getMessage(), Instant.now()));
        AiAdvisorContext baseContext = new AiAdvisorContext(conversation, profile, List.of(), Map.of());
        List<org.springframework.ai.model.function.FunctionCallback> callbacks = toolManager.buildCallbacks(baseContext);
        AiAdvisorContext advisorContext = new AiAdvisorContext(conversation, profile, callbacks, Map.of("principalId", profile.getPrincipalId()));
        AiAdvisorContextHolder.set(advisorContext);
        AiStructuredReply structured = null;
        String content;
        try {
            CallResponseSpec callResponse = chatClient.prompt()
                    .user(user -> user.text(request.getMessage()))
                    .call();
            content = callResponse.content();
            try {
                structured = callResponse.entity(AiStructuredReply.class);
            } catch (Exception ex) {
                log.warn("解析结构化输出失败，回退至纯文本: {}", ex.getMessage());
            }
        } finally {
            AiAdvisorContextHolder.clear();
        }
        String assistantContent = content;
        if (structured != null && StringUtils.hasText(structured.getReply())) {
            assistantContent = structured.getReply();
        }
        conversation.appendMessage(new AiMessageRecord(AiMessageRole.ASSISTANT, assistantContent, Instant.now()));
        emitChunks(assistantContent, chunkConsumer);
        return buildResponse(conversation, structured, assistantContent);
    }

    private void emitChunks(String content, Consumer<String> chunkConsumer) {
        if (chunkConsumer == null || !StringUtils.hasText(content)) {
            return;
        }
        int length = content.length();
        int chunkSize = Math.max(24, length / 20);
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(length, i + chunkSize);
            chunkConsumer.accept(content.substring(i, end));
        }
    }

    private void sendEvent(ResponseBodyEmitter emitter, String type, Object payload) throws IOException {
        String json = objectMapper.writeValueAsString(Map.of("type", type, "data", payload));
        emitter.send(json + "\n");
    }

    private void sendEventSilently(ResponseBodyEmitter emitter, String type, Object payload) {
        try {
            sendEvent(emitter, type, payload);
        } catch (IOException ignore) {
        }
    }

    /**
     * 人工确认工具执行。
     */
    public AiToolConfirmationResponse confirmTool(String conversationId, AiToolConfirmationRequest request) {
        AiPrincipalProfile profile = resolvePrincipal();
        AiConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("会话不存在"));
        if (!conversation.getPrincipalProfile().getPrincipalId().equals(profile.getPrincipalId())) {
            throw new AiSafetyException("仅会话发起人可操作工具");
        }
        AiPendingToolCall pendingCall = conversation.findPendingToolCall(request.getToolCallId())
                .orElseThrow(() -> new IllegalArgumentException("工具请求不存在"));
        AiToolExecutionResult result = toolManager.confirmTool(conversation, request.getToolCallId(), request.isApproved(), request.getComment());
        AiAuthorizationLogDTO logEntry = new AiAuthorizationLogDTO();
        logEntry.setUserId(String.valueOf(profile.getPrincipalId()));
        logEntry.setRole(String.join(",", profile.getRoleCodes()));
        logEntry.setConversationId(conversationId);
        logEntry.setToolCallId(pendingCall.getId());
        logEntry.setToolName(pendingCall.getToolName());
        logEntry.setOperation(pendingCall.getOperationGuide() != null ? pendingCall.getOperationGuide().getTitle() : pendingCall.getSummary());
        logEntry.setApproved(request.isApproved());
        logEntry.setComment(request.getComment());
        logEntry.setParams(pendingCall.getParameterSnapshot());
        logEntry.setResult(result.getMessage());
        authorizationLogService.record(logEntry);
        AiToolConfirmationResponse response = new AiToolConfirmationResponse();
        response.setSuccess(result.isSuccess());
        response.setMessage(result.getMessage());
        conversation.findPendingToolCall(request.getToolCallId()).ifPresent(call -> response.setPendingTool(toPending(call)));
        return response;
    }

    /**
     * 查询会话详情，主要用于前端刷新历史记录。
     */
    public AiChatResponse detail(String conversationId) {
        AiConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("会话不存在"));
        AiStructuredReply structured = new AiStructuredReply();
        structured.setReply(conversation.getMessages().isEmpty()
                ? ""
                : conversation.getMessages().get(conversation.getMessages().size() - 1).getContent());
        AiChatResponse response = buildResponse(conversation, structured, structured.getReply());
        AiServiceStatusDTO skeleton = settingsService.createStatusSkeleton();
        String modelName = skeleton.getActiveChatModel();
        response.setModelName(modelName);
        response.setProviderName(settingsService.resolveProviderByModel(modelName).orElse(null));
        response.setStreaming(false);
        return response;
    }

    private AiChatResponse buildResponse(AiConversation conversation,
                                         AiStructuredReply structured,
                                         String fallbackContent) {
        AiChatResponse response = new AiChatResponse();
        response.setConversationId(conversation.getConversationId());
        response.setClosed(conversation.isClosed());
        conversation.getCloseReason().ifPresent(response::setCloseReason);
        response.setStructured(structured);
        if (structured == null) {
            AiStructuredReply fallback = new AiStructuredReply();
            fallback.setReply(fallbackContent);
            response.setStructured(fallback);
        }
        response.setHistory(conversation.getMessages().stream()
                .map(msg -> new AiMessageDTO(msg.getRole().name(), msg.getContent(), msg.getTimestamp()))
                .collect(Collectors.toList()));
        conversation.getPendingToolCalls().stream()
                .filter(call -> call.getStatus() == AiToolStatus.AWAITING_CONFIRMATION)
                .findFirst()
                .ifPresent(call -> response.setPendingTool(toPending(call)));
        return response;
    }

    private AiPendingToolDTO toPending(AiPendingToolCall call) {
        AiPendingToolDTO dto = new AiPendingToolDTO();
        dto.setToolCallId(call.getId());
        dto.setToolName(call.getToolName());
        dto.setDescription(call.getToolDescription());
        dto.setSummary(call.getSummary());
        dto.setStatus(call.getStatus().name());
        dto.setCreatedAt(call.getCreatedAt());
        dto.setRiskLevel(call.getOperationGuide() != null ? call.getOperationGuide().getRiskLevel() : null);
        dto.setParams(call.getParameterSnapshot());
        dto.setOperationGuide(call.getOperationGuide());
        return dto;
    }

    private AiConversation resolveConversation(String conversationId, AiPrincipalProfile profile) {
        if (StringUtils.hasText(conversationId)) {
            return conversationRepository.findById(conversationId)
                    .orElseGet(() -> conversationRepository.create(profile));
        }
        return conversationRepository.create(profile);
    }

    private AiPrincipalProfile resolvePrincipal() {
        return SecurityUtils.getAdminPrincipal()
                .map(this::fromAdmin)
                .orElseGet(() -> SecurityUtils.getPortalUserPrincipal()
                        .map(this::fromPortal)
                        .orElseThrow(() -> new AiSafetyException("用户未登录，无法使用 AI 能力")));
    }

    private AiPrincipalProfile fromAdmin(AdminPrincipal principal) {
        Set<String> allow = principal.getAllowPermissions();
        Set<String> deny = principal.getDeniedPermissions();
        return new AiPrincipalProfile(principal.getId(), AiPrincipalType.ADMIN, allow, deny, principal.getRoleCodes());
    }

    private AiPrincipalProfile fromPortal(PortalUserPrincipal principal) {
        return new AiPrincipalProfile(principal.getId(), AiPrincipalType.USER,
                Set.of("PORTAL_CONTENT:READ"), Set.of(), Set.of(principal.getRoleCode()));
    }
}
