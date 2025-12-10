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

    /**
     * 分页查询活动列表
     * 
     * @param query 分页查询参数，包含页码、每页条数以及筛选条件
     * @return 活动列表分页信息，包含活动的基本信息如名称、封面、时间等
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_READ)")
    public Result<PageInfo<ActivityManageVO>> page(@Valid ActivityManagePageQuery query) {
        log.info("【后台-活动管理】分页查询，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        return Result.success(activityManageService.page(query));
    }

    /**
     * 查询活动详情
     * 
     * @param id 活动ID
     * @return 活动详细信息，包括所有基础信息和扩展信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_READ)")
    public Result<ActivityManageDetailVO> detail(@PathVariable Long id) {
        log.info("【后台-活动管理】查询详情，活动ID={}", id);
        return Result.success(activityManageService.detail(id));
    }

    /**
     * 新增活动
     * 
     * @param dto 活动信息数据传输对象，包含活动的所有必要信息
     * @return 新创建的活动ID
     */
    @PostMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_CREATE)")
    public Result<Long> create(@Valid @RequestBody ActivityManageDTO dto) {
        log.info("【后台-活动管理】新增活动：{}", dto.getName());
        Long id = activityManageService.create(dto);
        return Result.success(id);
    }

    /**
     * 更新活动信息
     * 
     * @param id 活动ID
     * @param dto 活动更新信息数据传输对象
     * @return 操作结果
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_UPDATE)")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ActivityManageDTO dto) {
        log.info("【后台-活动管理】编辑活动，ID={}", id);
        activityManageService.update(id, dto);
        return Result.success();
    }

    /**
     * 删除活动
     * 
     * @param id 活动ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_MANAGE_DELETE)")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("【后台-活动管理】删除活动，ID={}", id);
        activityManageService.delete(id);
        return Result.success();
    }

    /**
     * 分页查询活动评论列表
     * 
     * @param id 活动ID
     * @param query 评论分页查询参数
     * @return 活动评论分页信息
     */
    @GetMapping("/{id}/comment/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_COMMENT_MANAGE)")
    public Result<PageInfo<ActivityCommentVO>> pageComments(@PathVariable Long id,
                                                            @Valid ActivityCommentManagePageQuery query) {
        PageInfo<ActivityCommentVO> pageInfo = activityCommentManageService.pageComments(id, query);
        return Result.success(pageInfo);
    }

    /**
     * 删除活动评论
     * 
     * @param commentId 评论ID
     * @return 操作结果
     */
    @DeleteMapping("/comment/{commentId}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).ACTIVITY_COMMENT_MANAGE)")
    public Result<Void> deleteComment(@PathVariable Long commentId) {
        activityCommentManageService.deleteComment(commentId);
        return Result.success();
    }
}