package aftnos.aftourismserver.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户信息修改请求体，供门户用户在个人中心更新资料时使用。
 */
@Data
public class UserInfoUpdateRequest {

    /** 昵称，允许为空字符串，最大 50 字 */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickName;

    /** 性别：男/女/未知，前端写入时会做枚举校验 */
    @Size(max = 10, message = "性别字段长度异常")
    private String gender;

    /** 联系电话，最大 20 字符 */
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String phone;

    /** 邮箱，可选，遵循标准格式 */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /** 头像地址，可选 */
    @Size(max = 255, message = "头像地址过长")
    private String avatar;

    /** 个人简介，存放在 t_user.remark 字段，最大 255 字 */
    @Size(max = 255, message = "个人简介长度不能超过255个字符")
    private String remark;
}
