package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.auth.mapper.RoleAccessMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.auth.pojo.RoleAccess;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RBAC 权限校验与主体构建服务。
 */
@Component("rbacAuthority")
public class RbacAuthorityService {

    private static final Logger log = LoggerFactory.getLogger(RbacAuthorityService.class);

    private final RoleAccessMapper roleAccessMapper;

    public RbacAuthorityService(RoleAccessMapper roleAccessMapper) {
        this.roleAccessMapper = roleAccessMapper;
    }

    /**
     * 根据数据库管理员信息构建安全主体。
     */
    public AdminPrincipal buildAdminPrincipal(Admin admin) {
        if (admin == null) {
            throw new UnauthorizedException("管理员不存在");
        }
        boolean superAdmin = admin.getIsSuper() != null && admin.getIsSuper() == 1;
        Set<String> roleCodes = parseRoleCodes(admin.getRoleCode());
        if (superAdmin) {
            roleCodes.add("SUPER_ADMIN");
        }
        if (roleCodes.isEmpty()) {
            roleCodes.add("ADMIN");
        }

        Set<String> allowPermissions = new HashSet<>();
        Set<String> denyPermissions = new HashSet<>();
        if (!roleCodes.isEmpty()) {
            List<RoleAccess> accesses = roleAccessMapper.findByRoleCodes(roleCodes);
            for (RoleAccess access : accesses) {
                String key = buildPermissionKey(access.getResourceKey(), access.getAction());
                if (access.getAllow() != null && access.getAllow() == 1) {
                    allowPermissions.add(key);
                } else {
                    denyPermissions.add(key);
                }
            }
        }

        Collection<? extends GrantedAuthority> authorities = roleCodes.stream()
                .map(code -> new SimpleGrantedAuthority("ROLE_" + code))
                .collect(Collectors.toSet());

        return AdminPrincipal.create(admin, superAdmin, authorities, roleCodes, allowPermissions, denyPermissions);
    }

    /**
     * 判断当前登录管理员是否拥有指定权限。
     */
    public boolean hasPermission(AdminPermission permission) {
        return hasPermission(permission.resourceKey(), permission.action());
    }

    /**
     * 判断当前登录管理员是否拥有资源-动作权限。
     */
    public boolean hasPermission(String resourceKey, String action) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AdminPrincipal principal)) {
            return false;
        }
        if (principal.isSuperAdmin()) {
            log.debug("超级管理员 {} 访问 {}:{} 自动放行", principal.getUsername(), resourceKey, action);
            return true;
        }
        if (isDenied(principal.getDeniedPermissions(), resourceKey, action)) {
            return false;
        }
        return isAllowed(principal.getAllowPermissions(), resourceKey, action);
    }

    private boolean isAllowed(Set<String> permissions, String resourceKey, String action) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        String exact = buildPermissionKey(resourceKey, action);
        if (permissions.contains(exact)) {
            return true;
        }
        if (permissions.contains(buildPermissionKey(resourceKey, "*"))) {
            return true;
        }
        if (permissions.contains(buildPermissionKey("*", action))) {
            return true;
        }
        return permissions.contains(buildPermissionKey("*", "*"));
    }

    private boolean isDenied(Set<String> permissions, String resourceKey, String action) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        String exact = buildPermissionKey(resourceKey, action);
        if (permissions.contains(exact)) {
            return true;
        }
        if (permissions.contains(buildPermissionKey(resourceKey, "*"))) {
            return true;
        }
        if (permissions.contains(buildPermissionKey("*", action))) {
            return true;
        }
        return permissions.contains(buildPermissionKey("*", "*"));
    }

    private Set<String> parseRoleCodes(String roleCodeStr) {
        if (roleCodeStr == null || roleCodeStr.trim().isEmpty()) {
            return new HashSet<>();
        }
        String[] parts = roleCodeStr.split(",");
        Set<String> codes = new HashSet<>();
        for (String part : parts) {
            String code = part.trim();
            if (!code.isEmpty()) {
                codes.add(code.toUpperCase());
            }
        }
        return codes;
    }

    private String buildPermissionKey(String resourceKey, String action) {
        String resource = resourceKey == null || resourceKey.trim().isEmpty()
                ? "*" : resourceKey.trim().toUpperCase();
        String act = action == null || action.trim().isEmpty()
                ? "*" : action.trim().toUpperCase();
        return resource + ":" + act;
    }
}
