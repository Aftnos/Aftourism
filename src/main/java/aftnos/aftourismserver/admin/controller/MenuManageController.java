package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.MenuPermissionSaveRequest;
import aftnos.aftourismserver.admin.dto.MenuSaveRequest;
import aftnos.aftourismserver.admin.service.MenuManageService;
import aftnos.aftourismserver.auth.vo.MenuRouteVO;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rbac/menus")
public class MenuManageController {

    private final MenuManageService menuManageService;

    /**
     * 查询菜单树。
     */
    @GetMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_READ)")
    public Result<List<MenuRouteVO>> listMenus() {
        log.info("【查询菜单树】");
        return Result.success(menuManageService.listMenuTree());
    }

    /**
     * 新增菜单。
     */
    @PostMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_CREATE)")
    public Result<Long> createMenu(@Valid @RequestBody MenuSaveRequest request) {
        log.info("【新增菜单】name={}", request.getName());
        return Result.success(menuManageService.createMenu(request));
    }

    /**
     * 更新菜单。
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_UPDATE)")
    public Result<Void> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuSaveRequest request) {
        log.info("【更新菜单】id={}", id);
        menuManageService.updateMenu(id, request);
        return Result.success();
    }

    /**
     * 删除菜单。
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_DELETE)")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        log.info("【删除菜单】id={}", id);
        menuManageService.deleteMenu(id);
        return Result.success();
    }

    /**
     * 新增菜单权限。
     */
    @PostMapping("/{menuId}/permissions")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_UPDATE)")
    public Result<Long> createPermission(@PathVariable Long menuId,
                                         @Valid @RequestBody MenuPermissionSaveRequest request) {
        log.info("【新增菜单权限】menuId={}, authMark={}", menuId, request.getAuthMark());
        return Result.success(menuManageService.createPermission(menuId, request));
    }

    /**
     * 更新菜单权限。
     */
    @PutMapping("/permissions/{permissionId}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_UPDATE)")
    public Result<Void> updatePermission(@PathVariable Long permissionId,
                                         @Valid @RequestBody MenuPermissionSaveRequest request) {
        log.info("【更新菜单权限】permissionId={}", permissionId);
        menuManageService.updatePermission(permissionId, request);
        return Result.success();
    }

    /**
     * 删除菜单权限。
     */
    @DeleteMapping("/permissions/{permissionId}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_DELETE)")
    public Result<Void> deletePermission(@PathVariable Long permissionId) {
        log.info("【删除菜单权限】permissionId={}", permissionId);
        menuManageService.deletePermission(permissionId);
        return Result.success();
    }
}
