package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员个人资料更新请求体。
 */
@Data
public class AdminProfileUpdateRequest {

    /** 真实姓名 */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    /** 联系电话 */
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String phone;

    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /** 头像地址 */
    @Size(max = 255, message = "头像地址长度不能超过255个字符")
    private String avatar;

    /** 个人介绍 */
    @Size(max = 255, message = "个人介绍长度不能超过255个字符")
    private String introduction;
}
