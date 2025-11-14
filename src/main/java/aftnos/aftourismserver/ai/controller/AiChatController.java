package aftnos.aftourismserver.ai.controller;

import aftnos.aftourismserver.ai.dto.AiChatRequest;
import aftnos.aftourismserver.ai.dto.AiChatResponse;
import aftnos.aftourismserver.ai.dto.AiToolConfirmationRequest;
import aftnos.aftourismserver.ai.dto.AiToolConfirmationResponse;
import aftnos.aftourismserver.ai.service.AiConversationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public AiChatResponse chat(@Valid @RequestBody AiChatRequest request) {
        return conversationService.chat(request);
    }

    @PostMapping("/{conversationId}/confirm")
    public AiToolConfirmationResponse confirm(@PathVariable String conversationId,
                                               @Valid @RequestBody AiToolConfirmationRequest request) {
        return conversationService.confirmTool(conversationId, request);
    }

    @GetMapping("/{conversationId}")
    public AiChatResponse detail(@PathVariable String conversationId) {
        return conversationService.detail(conversationId);
    }
}
