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
    /** 用户ID */
    Long userId;
    /** 登录账号 */
    String username;
    /** 用户昵称 */
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
