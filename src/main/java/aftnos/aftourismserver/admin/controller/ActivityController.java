package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.ActivityRejectDTO;
import aftnos.aftourismserver.admin.dto.ActivityAuditPageQuery;
import aftnos.aftourismserver.admin.service.ActivityService;
import aftnos.aftourismserver.admin.vo.ActivityAuditDetailVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
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
     * 审核活动分页查询
     * 示例：GET /admin/activity/audit/page?pageNum=1&pageSize=10&applyStatus=0
     * 返回字段包含：活动ID、活动名称、提交时间、审核状态等
     */
    @GetMapping("/audit/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_APPROVE)")
    public Result<PageInfo<aftnos.aftourismserver.admin.vo.ActivityAuditItemVO>> auditPage(@Valid ActivityAuditPageQuery query) {
        log.info("【后台-活动审核分页】收到请求，页码={}，每页条数={}，状态筛选={}", query.getPageNum(), query.getPageSize(), query.getApplyStatus());
        PageInfo<aftnos.aftourismserver.admin.vo.ActivityAuditItemVO> pageInfo = activityService.pageAudit(query);
        return Result.success(pageInfo);
    }

    /**
     * 查询活动详情
     */
    @GetMapping("/audit/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_APPROVE)")
    public Result<ActivityAuditDetailVO> detail(@PathVariable Long id) {
        log.info("【后台-活动审核详情】收到请求，申报ID={}", id);
        ActivityAuditDetailVO detail = activityService.detail(id);
        return Result.success(detail);
    }

}
