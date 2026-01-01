package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.RoleMenuAssignRequest;
import aftnos.aftourismserver.admin.dto.RoleMenuPermissionAssignRequest;

import java.util.List;

/**
 * 角色菜单与按钮权限配置服务。
 */
public interface RoleMenuPermissionService {

    /**
     * 查询角色已分配的菜单ID列表。
     *
     * @param roleCode 角色编码
     * @return 菜单ID列表
     */
    List<Long> listRoleMenuIds(String roleCode);

    /**
     * 保存角色菜单分配。
     *
     * @param roleCode 角色编码
     * @param request  分配请求
     */
    void saveRoleMenus(String roleCode, RoleMenuAssignRequest request);

    /**
     * 查询角色已分配的按钮权限ID列表。
     *
     * @param roleCode 角色编码
     * @return 按钮权限ID列表
     */
    List<Long> listRoleMenuPermissionIds(String roleCode);

    /**
     * 保存角色菜单按钮权限分配。
     *
     * @param roleCode 角色编码
     * @param request  分配请求
     */
    void saveRoleMenuPermissions(String roleCode, RoleMenuPermissionAssignRequest request);
}
