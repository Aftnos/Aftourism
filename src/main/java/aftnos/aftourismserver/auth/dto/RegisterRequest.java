package aftnos.aftourismserver.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求体，对应 t_user 表字段
 */
@Data
public class RegisterRequest {

    /** 登录账号（唯一） */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    private String username;

    /** 登录密码 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度需在6~100个字符之间")
    private String password;

    /** 昵称/姓名，可为空 */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    /** 联系电话，可为空 */
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String phone;

    /** 邮箱，可为空 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /** 头像地址，可为空 */
    @Size(max = 255, message = "头像地址长度不能超过255个字符")
    private String avatar;

    /** 备注信息，可为空 */
    @Size(max = 255, message = "备注长度不能超过255个字符")
    private String remark;
}
