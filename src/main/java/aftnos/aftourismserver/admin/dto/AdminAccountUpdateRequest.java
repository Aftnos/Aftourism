package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 更新管理员账户请求体。
 */
@Data
public class AdminAccountUpdateRequest {

    /** 新密码，可选 */
    @Size(min = 6, max = 100, message = "密码长度需在 6-100 位之间")
    private String password;

    /** 真实姓名 */
    @Size(max = 50, message = "姓名长度不能超过 50 字符")
    private String realName;

    /** 联系电话 */
    @Size(max = 20, message = "电话长度不能超过 20 字符")
    private String phone;

    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过 100 字符")
    private String email;

    /** 角色编码集合，可为空表示不调整 */
    private List<@Size(min = 1, max = 100, message = "角色编码长度需在 1-100 之间") String> roleCodes;

    /** 是否超级管理员 */
    private Boolean superAdmin;

    /** 状态 */
    private Integer status;

    /** 备注 */
    @Size(max = 255, message = "备注长度不能超过 255 字符")
    private String remark;
}
