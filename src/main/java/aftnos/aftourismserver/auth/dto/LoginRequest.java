package aftnos.aftourismserver.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求体。
 * <p>兼容前端文档的字段命名：userName 与 password，
 * 通过 {@link JsonProperty} 注解确保 JSON 反序列化正确映射。</p>
 */
@Data
public class LoginRequest {

    /**
     * 登录账号，接口字段名为 userName。
     */
    @JsonProperty("userName")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 登录密码。
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 获取去除首尾空格的账号，便于后续校验。
     */
    public String normalizedUsername() {
        return userName == null ? null : userName.trim();
    }
}
