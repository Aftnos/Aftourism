package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.auth.service.AuthService;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.PrincipalType;
import aftnos.aftourismserver.common.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 统一登录服务实现。
 * <p>根据用户名自动匹配管理员或门户用户，并完成密码校验、状态校验与令牌生成。</p>
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AdminMapper adminMapper,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils) {
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String username = request.normalizedUsername();
        if (!StringUtils.hasText(username)) {
            throw new BusinessException("用户名不能为空");
        }

        // 优先匹配管理员账号，随后匹配门户用户账号。
        Admin admin = adminMapper.findByUsername(username);
        if (admin != null) {
            return handleAdminLogin(request, admin);
        }

        User user = userMapper.findByUsername(username);
        if (user != null) {
            return handlePortalUserLogin(request, user);
        }

        throw new BusinessException("用户名或密码错误");
    }

    /**
     * 管理员登录处理。
     */
    private LoginResponse handleAdminLogin(LoginRequest request, Admin admin) {
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (admin.getIsDeleted() != null && admin.getIsDeleted() == 1) {
            throw new BusinessException("用户名或密码错误");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new BusinessException("账号已停用");
        }

        PrincipalType type = (admin.getIsSuper() != null && admin.getIsSuper() == 1)
                ? PrincipalType.SUPER_ADMIN
                : PrincipalType.ADMIN;
        String token = jwtUtils.generateToken(admin.getId(), type);
        String refreshToken = jwtUtils.generateRefreshToken(admin.getId(), type);
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 门户用户登录处理。
     */
    private LoginResponse handlePortalUserLogin(LoginRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已停用");
        }

        String token = jwtUtils.generateToken(user.getId(), PrincipalType.PORTAL_USER);
        String refreshToken = jwtUtils.generateRefreshToken(user.getId(), PrincipalType.PORTAL_USER);
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
