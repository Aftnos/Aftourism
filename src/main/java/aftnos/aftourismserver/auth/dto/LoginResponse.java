package aftnos.aftourismserver.auth.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Set;

/**
 * 登录成功返回的核心数据
 */
@Value
@Builder
public class LoginResponse {
    /** 主体ID，可对应用户ID或管理员ID */
    Long principalId;
    /** 主体类型：PORTAL_USER / ADMIN / SUPER_ADMIN */
    String principalType;
    /** 用户ID（兼容旧字段，门户用户返回相同ID） */
    Long userId;
    /** 登录账号 */
    String username;
    /** 显示名称（门户为昵称，后台为真实姓名） */
    String nickname;
    /** 头像地址 */
    String avatar;
    /** 联系电话 */
    String phone;
    /** 邮箱 */
    String email;
    /** 当前状态 */
    Integer status;
    /** 是否超级管理员（仅后台登录场景使用） */
    Boolean superAdmin;
    /** 角色编码集合 */
    Set<String> roles;
    /** 允许的权限键集合 */
    Set<String> permissions;
    /** JWT */
    String token;
    /** 过期时间 */
    Instant expiresAt;
}
