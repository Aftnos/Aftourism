package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.RoleMenuAssignRequest;
import aftnos.aftourismserver.admin.dto.RoleMenuPermissionAssignRequest;
import aftnos.aftourismserver.admin.service.RoleMenuPermissionService;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色菜单与按钮权限配置接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rbac/roles")
public class RoleMenuPermissionController {

    private final RoleMenuPermissionService roleMenuPermissionService;

    /**
     * 查询角色已分配的菜单列表。
     */
    @GetMapping("/{roleCode}/menus")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_UPDATE)")
    public Result<List<Long>> listRoleMenus(@PathVariable String roleCode) {
        log.info("【查询角色菜单】roleCode={}", roleCode);
        return Result.success(roleMenuPermissionService.listRoleMenuIds(roleCode));
    }

    /**
     * 保存角色菜单分配。
     */
    @PostMapping("/{roleCode}/menus")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MENU_UPDATE)")
    public Result<Void> saveRoleMenus(@PathVariable String roleCode,
                                      @Valid @RequestBody RoleMenuAssignRequest request) {
        log.info("【保存角色菜单】roleCode={}", roleCode);
        roleMenuPermissionService.saveRoleMenus(roleCode, request);
        return Result.success();
    }

    /**
     * 查询角色已分配的按钮权限列表。
     */
    @GetMapping("/{roleCode}/menu-permissions")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ROLE_ACCESS_UPDATE)")
    public Result<List<Long>> listRoleMenuPermissions(@PathVariable String roleCode) {
        log.info("【查询角色按钮权限】roleCode={}", roleCode);
        return Result.success(roleMenuPermissionService.listRoleMenuPermissionIds(roleCode));
    }

    /**
     * 保存角色按钮权限分配。
     */
    @PostMapping("/{roleCode}/menu-permissions")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ROLE_ACCESS_UPDATE)")
    public Result<Void> saveRoleMenuPermissions(@PathVariable String roleCode,
                                                @Valid @RequestBody RoleMenuPermissionAssignRequest request) {
        log.info("【保存角色按钮权限】roleCode={}", roleCode);
        roleMenuPermissionService.saveRoleMenuPermissions(roleCode, request);
        return Result.success();
    }
}
