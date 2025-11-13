package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.auth.service.AdminAuthService;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.PrincipalType;
import aftnos.aftourismserver.common.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 管理员登录服务实现。
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
        Admin admin = adminMapper.findByUsername(request.getUsername());
        if (admin == null) {
            throw new BusinessException("账号或密码错误");
        }
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        if (admin.getIsDeleted() != null && admin.getIsDeleted() == 1) {
            throw new BusinessException("账号或密码错误");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new BusinessException("账号已停用");
        }

        String token = jwtUtils.generateToken(admin.getId(), PrincipalType.ADMIN);
        return LoginResponse.builder()
                .principalId(admin.getId())
                .principalType(PrincipalType.ADMIN.name())
                .userId(admin.getId())
                .username(admin.getUsername())
                .nickname(admin.getRealName())
                .phone(admin.getPhone())
                .email(admin.getEmail())
                .status(admin.getStatus())
                .token(token)
                .expiresAt(jwtUtils.calculateExpiryInstant())
                .build();
    }
}
