package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.ExchangeArticleAuditDTO;
import aftnos.aftourismserver.admin.dto.ExchangeArticleManagePageQuery;
import aftnos.aftourismserver.admin.service.ExchangeArticleManageService;
import aftnos.aftourismserver.admin.vo.ExchangeArticleManageVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交流文章管理接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/exchange/article")
public class ExchangeArticleManageController {

    private final ExchangeArticleManageService exchangeArticleManageService;

    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).EXCHANGE_ARTICLE_MANAGE)")
    public Result<PageInfo<ExchangeArticleManageVO>> page(@Valid ExchangeArticleManagePageQuery query) {
        PageInfo<ExchangeArticleManageVO> pageInfo = exchangeArticleManageService.page(query);
        return Result.success(pageInfo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).EXCHANGE_ARTICLE_MANAGE)")
    public Result<ExchangeArticleManageVO> detail(@PathVariable("id") Long id) {
        ExchangeArticleManageVO vo = exchangeArticleManageService.detail(id);
        return Result.success(vo);
    }

    @PutMapping("/{id}/audit")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).EXCHANGE_ARTICLE_MANAGE)")
    public Result<Void> audit(@PathVariable("id") Long id,
                              @Valid @RequestBody ExchangeArticleAuditDTO dto) {
        exchangeArticleManageService.audit(id, dto);
        return Result.success();
    }
}
