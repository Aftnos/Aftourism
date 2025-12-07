package aftnos.aftourismserver.common.exception;
/**
 * 用户错误异常
 */
public class UserErrorsException extends RuntimeException {
    public UserErrorsException(String message) {
        super(message);
    }
}
