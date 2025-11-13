package aftnos.aftourismserver.auth.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

/**
 * 登录成功返回的核心数据
 */
@Value
@Builder
public class LoginResponse {
    /** 主体ID，可对应用户ID或管理员ID */
    Long principalId;
    /** 主体类型：PORTAL_USER / ADMIN */
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
    /** JWT */
    String token;
    /** 过期时间 */
    Instant expiresAt;
}
