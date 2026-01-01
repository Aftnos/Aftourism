package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单分配请求参数。
 */
@Data
public class RoleMenuAssignRequest {

    /** 角色已选菜单ID列表 */
    @NotNull(message = "菜单ID列表不能为空")
    private List<Long> menuIds;
}
