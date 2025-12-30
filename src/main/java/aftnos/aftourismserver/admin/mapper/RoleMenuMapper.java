package aftnos.aftourismserver.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单与按钮权限关联 Mapper。
 */
@Mapper
public interface RoleMenuMapper {

    /**
     * 查询角色已分配的菜单ID列表。
     */
    List<Long> findMenuIdsByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询角色已分配的按钮权限ID列表。
     */
    List<Long> findPermissionIdsByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 删除角色全部菜单分配。
     */
    int deleteRoleMenus(@Param("roleCode") String roleCode);

    /**
     * 删除角色全部按钮权限分配。
     */
    int deleteRoleMenuPermissions(@Param("roleCode") String roleCode);

    /**
     * 批量插入角色菜单分配。
     */
    int batchInsertRoleMenus(@Param("roleCode") String roleCode, @Param("menuIds") List<Long> menuIds);

    /**
     * 批量插入角色按钮权限分配。
     */
    int batchInsertRoleMenuPermissions(@Param("roleCode") String roleCode,
                                       @Param("permissionIds") List<Long> permissionIds);
}
