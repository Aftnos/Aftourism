package aftnos.aftourismserver.ai.service;

import aftnos.aftourismserver.ai.context.AiAdvisorContext;
import aftnos.aftourismserver.ai.context.AiAdvisorContextHolder;
import aftnos.aftourismserver.ai.context.AiPrincipalProfile;
import aftnos.aftourismserver.ai.context.AiPrincipalType;
import aftnos.aftourismserver.ai.conversation.AiConversation;
import aftnos.aftourismserver.ai.conversation.AiMessageRecord;
import aftnos.aftourismserver.ai.conversation.AiMessageRole;
import aftnos.aftourismserver.ai.conversation.AiPendingToolCall;
import aftnos.aftourismserver.ai.conversation.AiToolStatus;
import aftnos.aftourismserver.ai.conversation.AiConversationRepository;
import aftnos.aftourismserver.ai.dto.*;
import aftnos.aftourismserver.ai.safety.AiSafetyException;
import aftnos.aftourismserver.ai.safety.AiSafetyService;
import aftnos.aftourismserver.ai.tool.AiToolExecutionResult;
import aftnos.aftourismserver.ai.tool.AiToolManager;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import aftnos.aftourismserver.common.security.PortalUserPrincipal;
import aftnos.aftourismserver.common.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public AiConversationService(ChatClient chatClient,
                                 AiConversationRepository conversationRepository,
                                 AiToolManager toolManager,
                                 AiSafetyService safetyService) {
        this.chatClient = chatClient;
        this.conversationRepository = conversationRepository;
        this.toolManager = toolManager;
        this.safetyService = safetyService;
    }

    /**
     * 处理用户输入，返回结构化响应。
     */
    public AiChatResponse chat(AiChatRequest request) {
        AiPrincipalProfile profile = resolvePrincipal();
        AiConversation conversation = resolveConversation(request.getConversationId(), profile);
        if (conversation.isClosed()) {
            AiChatResponse response = new AiChatResponse();
            response.setConversationId(conversation.getConversationId());
            response.setClosed(true);
            response.setCloseReason(conversation.getCloseReason().orElse("会话已结束，请重新发起"));
            return response;
        }
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
        AiChatResponse response = buildResponse(conversation, structured, assistantContent);
        return response;
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
        AiToolExecutionResult result = toolManager.confirmTool(conversation, request.getToolCallId(), request.isApproved(), request.getComment());
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
        return buildResponse(conversation, structured, structured.getReply());
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
