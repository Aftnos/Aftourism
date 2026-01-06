package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.auth.service.AdminAuthService;
import aftnos.aftourismserver.common.exception.UserErrorsException;
import aftnos.aftourismserver.common.security.PrincipalType;
import aftnos.aftourismserver.common.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 管理端登录服务实现。
 */
@Service
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AdminAuthServiceImpl(AdminMapper adminMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String username = request.normalizedUsername();
        if (!StringUtils.hasText(username)) {
            throw new UserErrorsException("用户名不能为空");
        }

        Admin admin = adminMapper.findByUsername(username);
        if (admin == null) {
            throw new UserErrorsException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new UserErrorsException("用户名或密码错误");
        }
        if (admin.getIsDeleted() != null && admin.getIsDeleted() == 1) {
            throw new UserErrorsException("用户名或密码错误");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new UserErrorsException("账号已停用");
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
}
