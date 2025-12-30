package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 角色管理查询条件，对齐前端 system/role 页面的搜索字段。
 */
@Data
public class SystemRoleSearchQuery {

    /** 当前页码，默认第一页 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer current = 1;

    /** 每页条数，默认 20，最大 100 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 200, message = "每页条数不能超过 100 条")
    private Integer size = 20;

    /** 角色名称模糊匹配 */
    private String roleName;

    /** 角色编码精准匹配 */
    private String roleCode;

    /** 角色描述关键词 */
    private String description;

    /** 状态筛选，前端布尔开关，后端仅用于占位 */
    private Boolean enabled;
}
