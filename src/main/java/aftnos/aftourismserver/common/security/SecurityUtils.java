package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 安全相关的便捷工具类。
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前认证信息。
     */
    public static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 获取当前登录的门户用户主体。
     */
    public static Optional<PortalUserPrincipal> getPortalUserPrincipal() {
        return getAuthentication()
                .filter(auth -> auth.getPrincipal() instanceof PortalUserPrincipal)
                .map(auth -> (PortalUserPrincipal) auth.getPrincipal());
    }

    /**
     * 获取当前登录的管理员主体。
     */
    public static Optional<AdminPrincipal> getAdminPrincipal() {
        return getAuthentication()
                .filter(auth -> auth.getPrincipal() instanceof AdminPrincipal)
                .map(auth -> (AdminPrincipal) auth.getPrincipal());
    }

    /**
     * 获取当前门户用户ID，若未登录则抛出未授权异常。
     */
    public static Long currentPortalUserIdOrThrow() {
        return getPortalUserPrincipal()
                .map(PortalUserPrincipal::getId)
                .orElseThrow(() -> new UnauthorizedException("用户未登录或登录状态已失效"));
    }

    /**
     * 获取当前管理员ID，若未登录则抛出未授权异常。
     */
    public static Long currentAdminIdOrThrow() {
        return getAdminPrincipal()
                .map(AdminPrincipal::getId)
                .orElseThrow(() -> new UnauthorizedException("管理员未登录或登录状态已失效"));
    }
}
