package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.ExchangeCommentUpdateDTO;
import aftnos.aftourismserver.admin.service.ExchangeCommentManageService;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.dto.ExchangeCommentPageQuery;
import aftnos.aftourismserver.portal.vo.ExchangeCommentVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交流评论管理接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/exchange")
public class ExchangeCommentManageController {

    private final ExchangeCommentManageService exchangeCommentManageService;

    @GetMapping("/article/{id}/comment/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).EXCHANGE_ARTICLE_MANAGE)")
    public Result<PageInfo<ExchangeCommentVO>> page(@PathVariable("id") Long articleId,
                                                    @Valid ExchangeCommentPageQuery query) {
        PageInfo<ExchangeCommentVO> pageInfo = exchangeCommentManageService.page(articleId, query);
        return Result.success(pageInfo);
    }

    @PutMapping("/comment/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).EXCHANGE_ARTICLE_MANAGE)")
    public Result<Void> update(@PathVariable("id") Long commentId,
                               @Valid @RequestBody ExchangeCommentUpdateDTO dto) {
        exchangeCommentManageService.update(commentId, dto);
        return Result.success();
    }

    @DeleteMapping("/comment/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).EXCHANGE_ARTICLE_MANAGE)")
    public Result<Void> delete(@PathVariable("id") Long commentId) {
        exchangeCommentManageService.delete(commentId);
        return Result.success();
    }
}
