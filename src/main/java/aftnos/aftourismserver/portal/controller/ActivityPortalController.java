package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.SecurityUtils;
import aftnos.aftourismserver.portal.dto.ActivityApplyDTO;
import aftnos.aftourismserver.portal.dto.ActivityCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.ActivityCommentPageQuery;
import aftnos.aftourismserver.portal.dto.ActivityPortalPageQuery;
import aftnos.aftourismserver.portal.service.ActivityCommentService;
import aftnos.aftourismserver.portal.service.ActivityPortalService;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import aftnos.aftourismserver.portal.vo.ActivitySummaryVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门户活动接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/activity")
public class ActivityPortalController {

    private final ActivityPortalService activityPortalService;
    private final ActivityCommentService activityCommentService;

    /**
     * 活动申报
     */
    @PostMapping("/apply")
    public Result<Long> apply(@Valid @RequestBody ActivityApplyDTO dto) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        log.info("【门户-活动申报】收到请求，用户ID={}，活动名称={}", userId, dto.getName());
        Long id = activityPortalService.apply(dto, userId);
        return Result.success(id);
    }

    /**
     * 门户分页查询活动
     */
    @GetMapping("/page")
    public Result<PageInfo<ActivitySummaryVO>> page(@Valid ActivityPortalPageQuery query) {
        log.info("【门户-分页查询活动】收到请求，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageInfo<ActivitySummaryVO> pageInfo = activityPortalService.pageActivities(query);
        return Result.success(pageInfo);
    }

    /**
     * 新增活动留言
     */
    @PostMapping("/{id}/comment")
    public Result<Long> addComment(@PathVariable("id") Long activityId,
                                   @Valid @RequestBody ActivityCommentCreateDTO dto) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        log.info("【门户-活动留言】收到留言请求，活动ID={}，用户ID={}", activityId, userId);
        Long commentId = activityCommentService.addComment(activityId, dto, userId);
        return Result.success(commentId);
    }

    /**
     * 分页查询活动留言
     */
    @GetMapping("/{id}/comment/page")
    public Result<PageInfo<ActivityCommentVO>> pageComments(@PathVariable("id") Long activityId,
                                                            @Valid ActivityCommentPageQuery query) {
        log.info("【门户-活动留言】收到分页查询请求，活动ID={}，parentId={}", activityId, query.getParentId());
        PageInfo<ActivityCommentVO> pageInfo = activityCommentService.pageComments(activityId, query);
        return Result.success(pageInfo);
    }
}
