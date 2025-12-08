package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.SystemRoleSearchQuery;
import aftnos.aftourismserver.admin.dto.SystemUserSearchQuery;
import aftnos.aftourismserver.admin.service.SystemManageService;
import aftnos.aftourismserver.admin.vo.SystemRoleVO;
import aftnos.aftourismserver.admin.vo.SystemUserVO;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.AdminPermission;
import aftnos.aftourismserver.common.vo.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * System Manage 接口，对应前端 web/admin/src/api/system-manage.ts。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class SystemManageController {

    private final SystemManageService systemManageService;

    /**
     * 用户管理 - 列表查询。
     */
    @GetMapping("/user/list")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).PORTAL_USER_READ)")
    public Result<PageResponse<SystemUserVO>> listUsers(@Valid SystemUserSearchQuery query) {
        log.info("【用户列表】page={}, size={}", query.getCurrent(), query.getSize());
        return Result.success(systemManageService.pageUsers(query));
    }

    /**
     * 角色管理 - 列表查询。
     */
    @GetMapping("/role/list")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ROLE_ACCESS_READ)")
    public Result<PageResponse<SystemRoleVO>> listRoles(@Valid SystemRoleSearchQuery query) {
        log.info("【角色列表】page={}, size={}", query.getCurrent(), query.getSize());
        return Result.success(systemManageService.pageRoles(query));
    }
}
