package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.Menu;
import aftnos.aftourismserver.auth.pojo.MenuPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 菜单及按钮权限 Mapper，负责 RBAC 前端菜单相关的读写。
 */
@Mapper
public interface MenuMapper {

    /** 查询启用的全部菜单（用于超级管理员）。 */
    List<Menu> selectEnabledMenus();

    /** 根据角色集合查询可访问菜单。 */
    List<Menu> selectMenusByRoleCodes(@Param("roleCodes") Collection<String> roleCodes);

    /** 查询指定菜单ID集合下的全部按钮权限。 */
    List<MenuPermission> selectPermissionsByMenuIds(@Param("menuIds") Collection<Long> menuIds);

    /** 根据角色集合查询已分配的按钮权限。 */
    List<MenuPermission> selectPermissionsByRoleCodes(@Param("roleCodes") Collection<String> roleCodes);

    /** 查询系统中所有按钮权限（超级管理员用）。 */
    List<MenuPermission> selectAllPermissions();
}
