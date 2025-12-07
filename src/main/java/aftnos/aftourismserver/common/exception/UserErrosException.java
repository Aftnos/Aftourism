package aftnos.aftourismserver.common.exception;

/**
 * 用户错误异常
 */

public class UserErrosException extends RuntimeException {
    public UserErrosException(String message) {
        super(message);
    }
}
