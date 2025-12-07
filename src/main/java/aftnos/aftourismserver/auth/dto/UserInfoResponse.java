package aftnos.aftourismserver.auth.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * 用户信息响应对象，对应 docs/login/login.md 中用户信息接口的字段定义。
 */
@Value
@Builder
public class UserInfoResponse {
    /** 用户主键 ID。 */
    Long userId;
    /** 登录账号/用户名。 */
    String userName;
    /** 角色编码列表，包含当前用户具备的全部角色。 */
    List<String> roles;
    /** 可用按钮编码列表，对应权限定义表中的权限键。 */
    List<String> buttons;
    /** 邮箱地址。 */
    String email;
}
