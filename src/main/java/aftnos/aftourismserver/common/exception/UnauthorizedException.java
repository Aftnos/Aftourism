package aftnos.aftourismserver.common.exception;

/**
 * 未登录或登录失效异常
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
