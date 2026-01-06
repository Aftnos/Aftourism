package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.auth.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 门户用户安全主体，实现 {@link UserDetails} 以便写入 Spring Security 上下文。
 */
public class PortalUserPrincipal implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Integer status;
    private final String nickname;
    private final String avatar;
    private final String phone;
    private final String email;
    private final String roleCode;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String gender;
    private final String remark;
    private final boolean advanced;

    private PortalUserPrincipal(Long id, String username, String password, Integer status,
                                String nickname, String avatar, String phone, String email,
                                String roleCode, String gender, String remark, boolean advanced,
                                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.nickname = nickname;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.roleCode = roleCode;
        this.gender = gender;
        this.remark = remark;
        this.advanced = advanced;
        this.authorities = authorities;
    }

    /**
     * 使用数据库实体快速构建安全主体。
     */
    public static PortalUserPrincipal fromUser(User user) {
        String role = user.getRoleCode() != null && !user.getRoleCode().trim().isEmpty()
                ? user.getRoleCode().trim().toUpperCase()
                : "PORTAL_USER";
        // 中文注释：兼容数据库中已包含 ROLE_ 前缀的历史数据，避免出现 ROLE_ROLE_ 前缀导致权限失效
        if (role.startsWith("ROLE_")) {
            role = role.substring("ROLE_".length());
        }
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
        return new PortalUserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                user.getNickname(),
                user.getAvatar(),
                user.getPhone(),
                user.getEmail(),
                role,
                user.getGender(),
                user.getRemark(),
                user.getIsAdvanced() != null && user.getIsAdvanced() == 1,
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleCode() {
        return roleCode;
    }
    
    public String getGender() {
        return gender;
    }
    
    public String getRemark() {
        return remark;
    }

    public boolean isAdvanced() {
        return advanced;
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
