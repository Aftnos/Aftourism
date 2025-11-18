package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.ActivityCommentManagePageQuery;
import aftnos.aftourismserver.admin.dto.ActivityManageDTO;
import aftnos.aftourismserver.admin.dto.ActivityManagePageQuery;
import aftnos.aftourismserver.admin.service.ActivityCommentManageService;
import aftnos.aftourismserver.admin.service.ActivityManageService;
import aftnos.aftourismserver.admin.vo.ActivityManageDetailVO;
import aftnos.aftourismserver.admin.vo.ActivityManageVO;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 活动管理接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/activity/manage")
public class ActivityManageController {

    private final ActivityManageService activityManageService;
    private final ActivityCommentManageService activityCommentManageService;

    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_READ)")
    public Result<PageInfo<ActivityManageVO>> page(@Valid ActivityManagePageQuery query) {
        log.info("【后台-活动管理】分页查询，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        return Result.success(activityManageService.page(query));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_READ)")
    public Result<ActivityManageDetailVO> detail(@PathVariable Long id) {
        log.info("【后台-活动管理】查询详情，活动ID={}", id);
        return Result.success(activityManageService.detail(id));
    }

    @PostMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_CREATE)")
    public Result<Long> create(@Valid @RequestBody ActivityManageDTO dto) {
        log.info("【后台-活动管理】新增活动：{}", dto.getName());
        Long id = activityManageService.create(dto);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_UPDATE)")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ActivityManageDTO dto) {
        log.info("【后台-活动管理】编辑活动，ID={}", id);
        activityManageService.update(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_DELETE)")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("【后台-活动管理】删除活动，ID={}", id);
        activityManageService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/comment/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_COMMENT_MANAGE)")
    public Result<PageInfo<ActivityCommentVO>> pageComments(@PathVariable Long id,
                                                            @Valid ActivityCommentManagePageQuery query) {
        PageInfo<ActivityCommentVO> pageInfo = activityCommentManageService.pageComments(id, query);
        return Result.success(pageInfo);
    }

    @DeleteMapping("/comment/{commentId}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_COMMENT_MANAGE)")
    public Result<Void> deleteComment(@PathVariable Long commentId) {
        activityCommentManageService.deleteComment(commentId);
        return Result.success();
    }
}
