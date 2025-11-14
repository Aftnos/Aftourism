package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.RolePermissionUpsertRequest;
import aftnos.aftourismserver.admin.service.RoleAccessManageService;
import aftnos.aftourismserver.admin.vo.PermissionDefinitionVO;
import aftnos.aftourismserver.admin.vo.RoleSummaryVO;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色权限配置接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rbac")
public class RoleAccessController {

    private final RoleAccessManageService roleAccessManageService;

    /**
     * 查询全部角色权限配置。
     */
    @GetMapping("/roles")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ROLE_ACCESS_READ)")
    public Result<List<RoleSummaryVO>> listRoles() {
        log.info("【查询角色权限列表】");
        return Result.success(roleAccessManageService.listRoles());
    }

    /**
     * 查询权限点目录，便于前端展示。
     */
    @GetMapping("/permissions/catalog")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ROLE_ACCESS_READ)")
    public Result<List<PermissionDefinitionVO>> permissionCatalog() {
        log.info("【查询权限点目录】");
        return Result.success(roleAccessManageService.permissionCatalog());
    }

    /**
     * 保存角色权限配置。
     */
    @PostMapping("/roles/permissions")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ROLE_ACCESS_UPDATE)")
    public Result<Void> savePermissions(@Valid @RequestBody RolePermissionUpsertRequest request) {
        log.info("【保存角色权限】roleCode={}", request.getRoleCode());
        roleAccessManageService.saveRolePermissions(request);
        return Result.success();
    }
}
