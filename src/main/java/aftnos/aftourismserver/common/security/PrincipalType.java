package aftnos.aftourismserver.common.security;

/**
 * JWT 中标识的主体类型。
 */
public enum PrincipalType {
    /** 前台门户用户 */
    PORTAL_USER,
    /** 管理员 */
    ADMIN,
    /** 超级管理员 */
    SUPER_ADMIN
}
