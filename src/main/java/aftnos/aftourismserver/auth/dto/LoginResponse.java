package aftnos.aftourismserver.auth.dto;

import lombok.Builder;
import lombok.Value;

/**
 * 登录成功返回的数据结构。
 */
@Value
@Builder
public class LoginResponse {
    /** 访问令牌，用于后续接口认证。 */
    String token;
    /** 刷新令牌，用于重新获取访问令牌。 */
    String refreshToken;
}
