package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员修改密码请求体。
 */
@Data
public class AdminPasswordUpdateRequest {

    /** 当前密码 */
    @NotBlank(message = "当前密码不能为空")
    @Size(max = 100, message = "当前密码长度不能超过100个字符")
    private String currentPassword;

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 100, message = "新密码长度需在6到100个字符之间")
    private String newPassword;

    /** 确认新密码 */
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 100, message = "确认密码长度需在6到100个字符之间")
    private String confirmPassword;
}
