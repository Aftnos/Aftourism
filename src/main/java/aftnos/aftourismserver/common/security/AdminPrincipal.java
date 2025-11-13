package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.auth.pojo.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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

    private AdminPrincipal(Long id, String username, String password, Integer status,
                           String realName, String phone, String email,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.realName = realName;
        this.phone = phone;
        this.email = email;
        this.authorities = authorities;
    }

    /**
     * 基于实体构建安全主体。
     */
    public static AdminPrincipal fromAdmin(Admin admin) {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new AdminPrincipal(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getStatus(),
                admin.getRealName(),
                admin.getPhone(),
                admin.getEmail(),
                authorities
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
