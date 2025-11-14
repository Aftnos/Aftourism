package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.PortalUserPageQuery;
import aftnos.aftourismserver.admin.dto.PortalUserUpdateRequest;
import aftnos.aftourismserver.admin.service.PortalUserManageService;
import aftnos.aftourismserver.admin.vo.PortalUserVO;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

/**
 * 门户用户后台管理接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rbac/users")
public class PortalUserManageController {

    private final PortalUserManageService portalUserManageService;

    /**
     * 分页查询门户用户。
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).PORTAL_USER_READ)")
    public Result<PageInfo<PortalUserVO>> page(@Valid PortalUserPageQuery query) {
        log.info("【门户用户分页】pageNum={}, pageSize={}", query.getPageNum(), query.getPageSize());
        return Result.success(portalUserManageService.page(query));
    }

    /**
     * 更新门户用户角色或状态。
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).PORTAL_USER_UPDATE)")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody PortalUserUpdateRequest request) {
        log.info("【更新门户用户】id={}", id);
        portalUserManageService.update(id, request);
        return Result.success();
    }
}
