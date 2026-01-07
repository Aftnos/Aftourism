package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.SecurityUtils;
import aftnos.aftourismserver.portal.dto.MessageFeedbackCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.MessageFeedbackCommentPageQuery;
import aftnos.aftourismserver.portal.dto.MessageFeedbackCreateDTO;
import aftnos.aftourismserver.portal.dto.MessageFeedbackPageQuery;
import aftnos.aftourismserver.portal.service.MessageFeedbackCommentService;
import aftnos.aftourismserver.portal.service.MessageFeedbackService;
import aftnos.aftourismserver.portal.vo.MessageFeedbackCommentVO;
import aftnos.aftourismserver.portal.vo.MessageFeedbackVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门户留言反馈接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/feedback")
public class MessageFeedbackPortalController {

    private final MessageFeedbackService messageFeedbackService;
    private final MessageFeedbackCommentService messageFeedbackCommentService;

    /**
     * 提交留言反馈
     */
    @PostMapping
    public Result<Long> create(@Valid @RequestBody MessageFeedbackCreateDTO dto) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        log.info("【门户-留言反馈】收到提交请求，用户ID={}", userId);
        Long id = messageFeedbackService.createFeedback(userId, dto);
        return Result.success(id);
    }

    /**
     * 门户分页查询留言反馈
     */
    @GetMapping("/page")
    public Result<PageInfo<MessageFeedbackVO>> page(@Valid MessageFeedbackPageQuery query) {
        PageInfo<MessageFeedbackVO> pageInfo = messageFeedbackService.pageFeedback(query);
        return Result.success(pageInfo);
    }

    /**
     * 留言反馈详情
     */
    @GetMapping("/{id}")
    public Result<MessageFeedbackVO> detail(@PathVariable("id") Long id) {
        MessageFeedbackVO vo = messageFeedbackService.getDetail(id);
        return Result.success(vo);
    }

    /**
     * 新增留言反馈评论
     */
    @PostMapping("/{id}/comment")
    public Result<Long> addComment(@PathVariable("id") Long feedbackId,
                                   @Valid @RequestBody MessageFeedbackCommentCreateDTO dto) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        Long commentId = messageFeedbackCommentService.addComment(feedbackId, dto, userId);
        return Result.success(commentId);
    }

    /**
     * 分页查询留言反馈评论
     */
    @GetMapping("/{id}/comment/page")
    public Result<PageInfo<MessageFeedbackCommentVO>> pageComments(@PathVariable("id") Long feedbackId,
                                                                   @Valid MessageFeedbackCommentPageQuery query) {
        PageInfo<MessageFeedbackCommentVO> pageInfo = messageFeedbackCommentService.pageComments(feedbackId, query);
        return Result.success(pageInfo);
    }

    /**
     * 点赞留言反馈评论
     */
    @PostMapping("/comment/{commentId}/like")
    public Result<Void> likeComment(@PathVariable("commentId") Long commentId) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        messageFeedbackCommentService.likeComment(commentId, userId);
        return Result.success();
    }

    /**
     * 删除自己的留言反馈评论
     */
    @DeleteMapping("/comment/{commentId}")
    public Result<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        messageFeedbackCommentService.deleteOwnComment(commentId, userId);
        return Result.success();
    }
}
