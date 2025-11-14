package aftnos.aftourismserver.ai.safety;

/**
 * AI 安全检测未通过时抛出的异常。
 */
public class AiSafetyException extends RuntimeException {

    public AiSafetyException(String message) {
        super(message);
    }
}
