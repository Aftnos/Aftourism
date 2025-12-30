package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.MenuPermissionSaveRequest;
import aftnos.aftourismserver.admin.dto.MenuSaveRequest;
import aftnos.aftourismserver.auth.vo.MenuRouteVO;

import java.util.List;

/**
 * 菜单管理服务接口。
 */
public interface MenuManageService {

    /**
     * 查询菜单树及权限配置。
     */
    List<MenuRouteVO> listMenuTree();

    /**
     * 新增菜单。
     */
    Long createMenu(MenuSaveRequest request);

    /**
     * 更新菜单。
     */
    void updateMenu(Long id, MenuSaveRequest request);

    /**
     * 删除菜单。
     */
    void deleteMenu(Long id);

    /**
     * 新增菜单权限。
     */
    Long createPermission(Long menuId, MenuPermissionSaveRequest request);

    /**
     * 更新菜单权限。
     */
    void updatePermission(Long permissionId, MenuPermissionSaveRequest request);

    /**
     * 删除菜单权限。
     */
    void deletePermission(Long permissionId);
}
