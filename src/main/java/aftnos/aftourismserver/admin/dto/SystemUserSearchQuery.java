package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 用户管理查询条件，对齐前端 system/user 页面的搜索字段。
 */
@Data
public class SystemUserSearchQuery {

    /** 当前页码，默认第一页 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer current = 1;

    /** 每页数量，默认 20，最大 100 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100 条")
    private Integer size = 20;

    /** 用户名模糊搜索 */
    private String userName;

    /** 昵称模糊搜索 */
    private String nickName;

    /** 性别筛选，占位字段 */
    private String userGender;

    /** 手机号模糊搜索 */
    private String userPhone;

    /** 邮箱模糊搜索 */
    private String userEmail;

    /** 状态筛选（1 启用，0 禁用） */
    private Integer status;
}
