package aftnos.aftourismserver.common.interceptor;

import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    /** 请求中保存用户ID的属性名 */
    public static final String ATTR_USER_ID = "CURRENT_USER_ID";
    /** 请求中保存完整用户对象的属性名 */
    public static final String ATTR_USER_INFO = "CURRENT_USER_INFO";

    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public JwtAuthenticationInterceptor(JwtUtils jwtUtils, UserMapper userMapper) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtUtils.parseUserId(token);
            // 根据 Token 中的用户ID查询数据库，确保与最新状态保持一致
            User user = userMapper.findById(userId);
            if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
                throw new UnauthorizedException("用户不存在或已删除");
            }
            if (user.getStatus() != null && user.getStatus() == 0) {
                throw new UnauthorizedException("用户已被禁用");
            }
            // 将用户信息写入请求，方便后续业务使用
            request.setAttribute(ATTR_USER_ID, userId);
            request.setAttribute(ATTR_USER_INFO, user);
            return true;
        }
        throw new UnauthorizedException("未登录或登录状态已失效");
    }
}
