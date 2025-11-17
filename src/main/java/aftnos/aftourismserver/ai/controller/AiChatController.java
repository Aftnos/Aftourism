package aftnos.aftourismserver.ai.controller;

import aftnos.aftourismserver.ai.dto.AiChatRequest;
import aftnos.aftourismserver.ai.dto.AiChatResponse;
import aftnos.aftourismserver.ai.dto.AiToolConfirmationRequest;
import aftnos.aftourismserver.ai.dto.AiToolConfirmationResponse;
import aftnos.aftourismserver.ai.service.AiConversationService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * AI 对话接口入口，所有工具调用均在服务端完成。
 */
@RestController
@RequestMapping("/ai/conversations")
public class AiChatController {

    private final AiConversationService conversationService;

    public AiChatController(AiConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/chat")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ASSIST_USE)")
    public AiChatResponse chat(@Valid @RequestBody AiChatRequest request) {
        return conversationService.chat(request);
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ASSIST_USE)")
    public ResponseBodyEmitter chatStream(@Valid @RequestBody AiChatRequest request) {
        return conversationService.chatStream(request);
    }

    @PostMapping("/{conversationId}/confirm")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ASSIST_USE)")
    public AiToolConfirmationResponse confirm(@PathVariable String conversationId,
                                               @Valid @RequestBody AiToolConfirmationRequest request) {
        return conversationService.confirmTool(conversationId, request);
    }

    @GetMapping("/{conversationId}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ASSIST_USE)")
    public AiChatResponse detail(@PathVariable String conversationId) {
        return conversationService.detail(conversationId);
    }
}
