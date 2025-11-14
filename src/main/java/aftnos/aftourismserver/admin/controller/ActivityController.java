package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.ActivityRejectDTO;
import aftnos.aftourismserver.admin.service.ActivityService;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 活动后台接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/activity")
public class ActivityController {

    private final ActivityService activityService;

    /**
     * 审核通过活动
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_APPROVE)")
    public Result<Void> approve(@PathVariable Long id) {
        log.info("【后台-活动审核通过】收到请求，活动ID={}", id);
        activityService.approve(id);
        return Result.success();
    }

    /**
     * 审核驳回活动
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_REJECT)")
    public Result<Void> reject(@PathVariable Long id, @Valid @RequestBody ActivityRejectDTO dto) {
        log.info("【后台-活动审核驳回】收到请求，活动ID={}", id);
        activityService.reject(id, dto.getRejectReason());
        return Result.success();
    }

    /**
     * 上线活动
     */
    @PutMapping("/{id}/online")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_ONLINE)")
    public Result<Void> online(@PathVariable Long id) {
        log.info("【后台-活动上线】收到请求，活动ID={}", id);
        activityService.online(id);
        return Result.success();
    }

    /**
     * 下线活动
     */
    @PutMapping("/{id}/offline")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_OFFLINE)")
    public Result<Void> offline(@PathVariable Long id) {
        log.info("【后台-活动下线】收到请求，活动ID={}", id);
        activityService.offline(id);
        return Result.success();
    }
}
