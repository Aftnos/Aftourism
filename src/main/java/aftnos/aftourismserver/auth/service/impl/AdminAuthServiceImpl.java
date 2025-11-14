package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.auth.service.AdminAuthService;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import aftnos.aftourismserver.common.security.PrincipalType;
import aftnos.aftourismserver.common.security.RbacAuthorityService;
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
    private final RbacAuthorityService rbacAuthorityService;

    public AdminAuthServiceImpl(AdminMapper adminMapper,
                                PasswordEncoder passwordEncoder,
                                JwtUtils jwtUtils,
                                RbacAuthorityService rbacAuthorityService) {
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.rbacAuthorityService = rbacAuthorityService;
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

        AdminPrincipal principal = rbacAuthorityService.buildAdminPrincipal(admin);
        PrincipalType principalType = principal.isSuperAdmin() ? PrincipalType.SUPER_ADMIN : PrincipalType.ADMIN;
        String token = jwtUtils.generateToken(admin.getId(), principalType);
        return LoginResponse.builder()
                .principalId(admin.getId())
                .principalType(principalType.name())
                .userId(admin.getId())
                .username(admin.getUsername())
                .nickname(admin.getRealName())
                .phone(admin.getPhone())
                .email(admin.getEmail())
                .status(admin.getStatus())
                .superAdmin(principal.isSuperAdmin())
                .roles(principal.getRoleCodes())
                .permissions(principal.getAllowPermissions())
                .token(token)
                .expiresAt(jwtUtils.calculateExpiryInstant())
                .build();
    }
}
