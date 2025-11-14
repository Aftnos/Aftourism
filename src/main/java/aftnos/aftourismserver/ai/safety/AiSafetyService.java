package aftnos.aftourismserver.ai.safety;

import aftnos.aftourismserver.ai.context.AiPrincipalProfile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 基础敏感内容与越狱检测逻辑，生产场景可接入三方服务。
 */
@Component
public class AiSafetyService {

    private static final List<String> BANNED_KEYWORDS = List.of(
            "暴恐", "涉黄", "武器", "自杀", "炸弹"
    );
    private static final List<String> JAILBREAK_PATTERNS = List.of(
            "忽略之前的指令", "越权", "暴露提示词", "系统提示"
    );
    private static final Pattern PII_PATTERN = Pattern.compile("(\\d{17}[0-9Xx]|1[3-9]\\d{9})");

    /**
     * 在进入大模型之前执行输入审查，不通过则抛出异常。
     */
    public void ensureSafe(String message, AiPrincipalProfile profile) {
        if (!StringUtils.hasText(message)) {
            throw new AiSafetyException("请求内容不能为空");
        }
        String normalized = message.toLowerCase();
        for (String keyword : BANNED_KEYWORDS) {
            if (normalized.contains(keyword.toLowerCase())) {
                throw new AiSafetyException("检测到敏感内容，已终止会话");
            }
        }
        for (String pattern : JAILBREAK_PATTERNS) {
            if (normalized.contains(pattern.toLowerCase())) {
                throw new AiSafetyException("检测到越权或越狱意图，请遵守使用规范");
            }
        }
        if (PII_PATTERN.matcher(message).find()) {
            throw new AiSafetyException("检测到疑似个人敏感信息，已终止会话");
        }
        if (profile.getType() == null) {
            throw new AiSafetyException("无法识别访问主体，出于安全考虑拒绝请求");
        }
    }
}
