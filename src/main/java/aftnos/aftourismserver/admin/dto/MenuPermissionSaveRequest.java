package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单按钮权限新增/修改请求参数。
 */
@Data
public class MenuPermissionSaveRequest {

    /** 权限名称 */
    @NotBlank(message = "权限名称不能为空")
    private String title;

    /** 权限标识 */
    @NotBlank(message = "权限标识不能为空")
    private String authMark;

    /** 排序值 */
    @NotNull(message = "排序值不能为空")
    private Integer sort;

    /** 备注 */
    private String remark;
}
