package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.PortalUserPrincipal;
import aftnos.aftourismserver.common.security.SecurityUtils;
import aftnos.aftourismserver.portal.dto.ExchangeArticleCreateDTO;
import aftnos.aftourismserver.portal.dto.ExchangeArticlePageQuery;
import aftnos.aftourismserver.portal.dto.ExchangeCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.ExchangeCommentPageQuery;
import aftnos.aftourismserver.portal.service.ExchangeArticleService;
import aftnos.aftourismserver.portal.service.ExchangeCommentService;
import aftnos.aftourismserver.portal.vo.ExchangeArticleVO;
import aftnos.aftourismserver.portal.vo.ExchangeCommentVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户交流区接口
 */
@Slf4j
@RestController
@RequestMapping("/portal/exchange")
@RequiredArgsConstructor
public class ExchangePortalController {

    private final ExchangeArticleService exchangeArticleService;
    private final ExchangeCommentService exchangeCommentService;

    @PostMapping
    public Result<Long> create(@Valid @RequestBody ExchangeArticleCreateDTO dto) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        Long id = exchangeArticleService.createArticle(userId, dto);
        return Result.success(id);
    }

    @GetMapping("/page")
    public Result<PageInfo<ExchangeArticleVO>> page(@Valid ExchangeArticlePageQuery query) {
        PageInfo<ExchangeArticleVO> pageInfo = exchangeArticleService.pageArticles(query, null, false);
        return Result.success(pageInfo);
    }

    @GetMapping("/user/{userId}/page")
    public Result<PageInfo<ExchangeArticleVO>> pageByUser(@PathVariable("userId") Long userId,
                                                          @Valid ExchangeArticlePageQuery query) {
        Long currentUserId = SecurityUtils.getPortalUserPrincipal()
                .map(PortalUserPrincipal::getId)
                .orElse(null);
        boolean includeAll = currentUserId != null && currentUserId.equals(userId);
        PageInfo<ExchangeArticleVO> pageInfo = exchangeArticleService.pageArticles(query, userId, includeAll);
        return Result.success(pageInfo);
    }

    @GetMapping("/{id}")
    public Result<ExchangeArticleVO> detail(@PathVariable("id") Long id) {
        Long currentUserId = SecurityUtils.getPortalUserPrincipal()
                .map(PortalUserPrincipal::getId)
                .orElse(null);
        ExchangeArticleVO vo = exchangeArticleService.getDetail(id, currentUserId);
        return Result.success(vo);
    }

    @PostMapping("/{id}/like")
    public Result<Void> like(@PathVariable("id") Long id) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        exchangeArticleService.likeArticle(id, userId);
        return Result.success();
    }

    @PostMapping("/{id}/comment")
    public Result<Long> comment(@PathVariable("id") Long articleId,
                                @Valid @RequestBody ExchangeCommentCreateDTO dto) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        Long commentId = exchangeCommentService.addComment(articleId, dto, userId);
        return Result.success(commentId);
    }

    @GetMapping("/{id}/comment/page")
    public Result<PageInfo<ExchangeCommentVO>> commentPage(@PathVariable("id") Long articleId,
                                                           @Valid ExchangeCommentPageQuery query) {
        PageInfo<ExchangeCommentVO> pageInfo = exchangeCommentService.pageComments(articleId, query);
        return Result.success(pageInfo);
    }

    @PostMapping("/comment/{commentId}/like")
    public Result<Void> likeComment(@PathVariable("commentId") Long commentId) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        exchangeCommentService.likeComment(commentId, userId);
        return Result.success();
    }

    @DeleteMapping("/comment/{commentId}")
    public Result<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        exchangeCommentService.deleteOwnComment(commentId, userId);
        return Result.success();
    }
}
