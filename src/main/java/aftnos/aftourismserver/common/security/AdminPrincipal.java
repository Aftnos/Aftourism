package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.auth.pojo.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 管理员安全主体，实现 {@link UserDetails}。
 */
public class AdminPrincipal implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Integer status;
    private final String realName;
    private final String phone;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean superAdmin;
    private final Set<String> roleCodes;
    private final Set<String> allowPermissions;
    private final Set<String> deniedPermissions;

    private AdminPrincipal(Long id, String username, String password, Integer status,
                           String realName, String phone, String email,
                           Collection<? extends GrantedAuthority> authorities,
                           boolean superAdmin,
                           Set<String> roleCodes,
                           Set<String> allowPermissions,
                           Set<String> deniedPermissions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.realName = realName;
        this.phone = phone;
        this.email = email;
        this.authorities = authorities;
        this.superAdmin = superAdmin;
        this.roleCodes = roleCodes == null ? Collections.emptySet() : Collections.unmodifiableSet(roleCodes);
        this.allowPermissions = allowPermissions == null ? Collections.emptySet() : Collections.unmodifiableSet(allowPermissions);
        this.deniedPermissions = deniedPermissions == null ? Collections.emptySet() : Collections.unmodifiableSet(deniedPermissions);
    }

    /**
     * 构建包含角色与权限数据的安全主体。
     */
    public static AdminPrincipal create(Admin admin,
                                        boolean superAdmin,
                                        Collection<? extends GrantedAuthority> authorities,
                                        Collection<String> roleCodes,
                                        Collection<String> allowPermissions,
                                        Collection<String> deniedPermissions) {
        Set<String> roleSet = roleCodes == null ? new HashSet<>() : new HashSet<>(roleCodes);
        Set<String> allowSet = allowPermissions == null ? new HashSet<>() : new HashSet<>(allowPermissions);
        Set<String> denySet = deniedPermissions == null ? new HashSet<>() : new HashSet<>(deniedPermissions);
        return new AdminPrincipal(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getStatus(),
                admin.getRealName(),
                admin.getPhone(),
                admin.getEmail(),
                authorities,
                superAdmin,
                roleSet,
                allowSet,
                denySet
        );
    }

    public Long getId() {
        return id;
    }

    public String getRealName() {
        return realName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    /**
     * 当前管理员是否为超级管理员。
     */
    public boolean isSuperAdmin() {
        return superAdmin;
    }

    /**
     * 管理员绑定的角色编码集合。
     */
    public Set<String> getRoleCodes() {
        return roleCodes;
    }

    /**
     * 允许访问的权限键集合（RESOURCE:ACTION）。
     */
    public Set<String> getAllowPermissions() {
        return allowPermissions;
    }

    /**
     * 显式拒绝访问的权限键集合。
     */
    public Set<String> getDeniedPermissions() {
        return deniedPermissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == null || status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == null || status == 1;
    }
}
