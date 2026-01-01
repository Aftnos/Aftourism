package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单按钮权限分配请求参数。
 */
@Data
public class RoleMenuPermissionAssignRequest {

    /** 角色已选按钮权限ID列表 */
    @NotNull(message = "菜单权限ID列表不能为空")
    private List<Long> permissionIds;
}
