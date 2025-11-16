package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.auth.pojo.Admin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 管理员安全主体，实现 Spring Security 的 {@link UserDetails} 接口。
 * <p>
 * 该类封装了管理员用户的安全信息，包括基本资料、权限控制和账户状态等，
 * 用于在系统中进行身份认证和授权操作。
 * </p>
 */
public class AdminPrincipal implements UserDetails {

    /** 管理员唯一标识ID */
    @Getter
    private final Long id;
    
    /** 管理员登录用户名 */
    private final String username;
    
    /** 管理员登录密码（已加密） */
    private final String password;
    
    /** 管理员账户状态：null或1表示启用，其他值表示禁用 */
    private final Integer status;
    
    /** 管理员真实姓名 */
    @Getter
    private final String realName;
    
    /** 管理员联系电话 */
    @Getter
    private final String phone;
    
    /** 管理员邮箱地址 */
    @Getter
    private final String email;
    
    /** 管理员被授予的权限集合 */
    private final Collection<? extends GrantedAuthority> authorities;
    
    /**
     * 当前管理员是否为超级管理员。
     * <p>
     * 超级管理员拥有系统所有权限，不受权限控制限制。
     * </p>
     */
    @Getter
    private final boolean superAdmin;
    
    /**
     * 管理员绑定的角色编码集合。
     * <p>
     * 用于标识管理员所属的角色，一个管理员可以拥有多个角色。
     * </p>
     */
    @Getter
    private final Set<String> roleCodes;
    
    /**
     * 允许访问的权限键集合（格式：RESOURCE:ACTION）。
     * <p>
     * 定义了管理员可以访问的资源和操作权限列表。
     * 例如："user:list" 表示允许查看用户列表。
     * </p>
     */
    @Getter
    private final Set<String> allowPermissions;
    
    /**
     * 显式拒绝访问的权限键集合。
     * <p>
     * 即使在角色或其他途径中获得了某些权限，也可以通过此集合显式地拒绝特定权限。
     * </p>
     */
    @Getter
    private final Set<String> deniedPermissions;

    /**
     * 构造管理员安全主体实例
     *
     * @param id               管理员唯一标识ID
     * @param username         登录用户名
     * @param password         登录密码（已加密）
     * @param status           账户状态
     * @param realName         真实姓名
     * @param phone            联系电话
     * @param email            邮箱地址
     * @param authorities      授予的权限集合
     * @param superAdmin       是否为超级管理员
     * @param roleCodes        角色编码集合
     * @param allowPermissions 允许的权限键集合
     * @param deniedPermissions 拒绝的权限键集合
     */
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
     * <p>
     * 工厂方法，用于创建 AdminPrincipal 实例，将传入的管理员信息、权限信息封装成安全主体对象。
     * </p>
     *
     * @param admin             管理员基本信息对象
     * @param superAdmin        是否为超级管理员
     * @param authorities       授予的权限集合
     * @param roleCodes         角色编码集合
     * @param allowPermissions  允许的权限键集合
     * @param deniedPermissions 拒绝的权限键集合
     * @return 封装后的管理员安全主体对象
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

    /**
     * 获取授予管理员的权限集合
     *
     * @return 权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 获取管理员登录密码
     *
     * @return 已加密的登录密码
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 获取管理员登录用户名
     *
     * @return 登录用户名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 判断账户是否未过期
     * <p>
     * 目前系统中默认所有账户永不过期。
     * </p>
     *
     * @return 始终返回 true，表示账户未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 判断账户是否未锁定
     * <p>
     * 当账户状态为 null 或 1 时表示账户未被锁定，可以正常登录。
     * </p>
     *
     * @return true表示账户未锁定，false表示账户已被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return status == null || status == 1;
    }

    /**
     * 判断凭证(密码)是否未过期
     * <p>
     * 目前系统中默认所有凭证永不过期。
     * </p>
     *
     * @return 始终返回 true，表示凭证未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 判断账户是否启用
     * <p>
     * 当账户状态为 null 或 1 时表示账户处于启用状态。
     * </p>
     *
     * @return true表示账户已启用，false表示账户已被禁用
     */
    @Override
    public boolean isEnabled() {
        return status == null || status == 1;
    }
}