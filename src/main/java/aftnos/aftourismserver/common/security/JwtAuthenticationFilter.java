package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器，解析 Token 并将主体写入安全上下文。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final RbacAuthorityService rbacAuthorityService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   UserMapper userMapper,
                                   AdminMapper adminMapper,
                                   RbacAuthorityService rbacAuthorityService) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.adminMapper = adminMapper;
        this.rbacAuthorityService = rbacAuthorityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 修改为直接使用authHeader作为token，不再要求Bearer前缀
        String token = authHeader;
        
        // 如果仍然包含Bearer前缀，则去除它（为了向后兼容）
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        try {
            JwtUtils.JwtPayload payload = jwtUtils.parsePayload(token);

            switch (payload.principalType()) {
                case PORTAL_USER -> authenticatePortalUser(request, token, payload.principalId());
                case ADMIN, SUPER_ADMIN -> authenticateAdmin(request, token, payload.principalId());
                default -> throw new UnauthorizedException("不支持的主体类型");
            }
        } catch (UnauthorizedException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"" + e.getMessage() + "\",\"data\":null}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticatePortalUser(HttpServletRequest request, String token, Long userId) {
        User user = userMapper.findById(userId);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new UnauthorizedException("用户不存在或已删除");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new UnauthorizedException("账号已停用");
        }

        PortalUserPrincipal principal = PortalUserPrincipal.fromUser(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, token, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void authenticateAdmin(HttpServletRequest request, String token, Long adminId) {
        Admin admin = adminMapper.findById(adminId);
        if (admin == null || (admin.getIsDeleted() != null && admin.getIsDeleted() == 1)) {
            throw new UnauthorizedException("管理员不存在或已删除");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new UnauthorizedException("账号已停用");
        }

        AdminPrincipal principal = rbacAuthorityService.buildAdminPrincipal(admin);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, token, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}