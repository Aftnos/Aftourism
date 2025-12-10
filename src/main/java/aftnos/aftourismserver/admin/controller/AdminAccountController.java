package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.AdminAccountCreateRequest;
import aftnos.aftourismserver.admin.dto.AdminAccountPageQuery;
import aftnos.aftourismserver.admin.dto.AdminAccountUpdateRequest;
import aftnos.aftourismserver.admin.service.AdminAccountService;
import aftnos.aftourismserver.admin.vo.AdminAccountVO;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

/**
 * 管理员账户与角色管理接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rbac/admins")
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    /**
     * 分页查询管理员账户。
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ADMIN_ACCOUNT_READ)")
    public Result<PageInfo<AdminAccountVO>> page(@Valid AdminAccountPageQuery query) {
        log.info("【管理员分页】current={}, size={}", query.getCurrent(), query.getSize());
        return Result.success(adminAccountService.page(query));
    }

    /**
     * 查询管理员详情。
     */
    @GetMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ADMIN_ACCOUNT_READ)")
    public Result<AdminAccountVO> detail(@PathVariable Long id) {
        log.info("【管理员详情】id={}", id);
        return Result.success(adminAccountService.detail(id));
    }

    /**
     * 新建管理员并分配角色。
     */
    @PostMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ADMIN_ACCOUNT_CREATE)")
    public Result<Long> create(@Valid @RequestBody AdminAccountCreateRequest request) {
        log.info("【创建管理员】username={}", request.getUsername());
        return Result.success(adminAccountService.create(request));
    }

    /**
     * 更新管理员信息或角色。
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ADMIN_ACCOUNT_UPDATE)")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AdminAccountUpdateRequest request) {
        log.info("【更新管理员】id={}", id);
        adminAccountService.update(id, request);
        return Result.success();
    }

    /**
     * 逻辑删除管理员。
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ADMIN_ACCOUNT_DELETE)")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("【删除管理员】id={}", id);
        adminAccountService.delete(id);
        return Result.success();
    }
}
