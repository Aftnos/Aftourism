package aftnos.aftourismserver.ai.tool.impl;

import aftnos.aftourismserver.ai.tool.AiServerTool;
import aftnos.aftourismserver.ai.tool.AiToolCallContext;
import aftnos.aftourismserver.ai.tool.AiToolExecutionResult;
import aftnos.aftourismserver.ai.tool.AiToolScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 门户用户提报服务需求的工具，实际执行时仅记录日志方便人工跟进。
 */
@Component
public class UserFeedbackTool implements AiServerTool<UserFeedbackTool.UserFeedbackInput> {

    private static final Logger log = LoggerFactory.getLogger(UserFeedbackTool.class);

    @Override
    public String name() {
        return "user_service_feedback";
    }

    @Override
    public String description() {
        return "帮助普通用户快速记录咨询/投诉/志愿申请等诉求";
    }

    @Override
    public AiToolScope scope() {
        return AiToolScope.USER;
    }

    @Override
    public Class<UserFeedbackInput> inputType() {
        return UserFeedbackInput.class;
    }

    @Override
    public String summarizeRequest(UserFeedbackInput input) {
        return "用户诉求类型=" + input.type() + "，主题=" + input.subject();
    }

    @Override
    public AiToolExecutionResult execute(AiToolCallContext<UserFeedbackInput> context) {
        UserFeedbackInput input = context.rawArguments();
        log.info("收到门户用户反馈，会话={}，主题={}，详情={}",
                context.conversation().getConversationId(),
                input.subject(),
                input.detail());
        return new AiToolExecutionResult(true,
                "已登记用户诉求，客服将尽快联系您",
                Map.of(
                        "subject", input.subject(),
                        "type", input.type(),
                        "comment", context.comment()
                ));
    }

    /**
     * 用户反馈结构。
     */
    public record UserFeedbackInput(String subject, String detail, String type) {
    }
}
