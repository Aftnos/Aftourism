package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.portal.dto.NewsPortalPageQuery;
import aftnos.aftourismserver.portal.service.NewsPortalService;
import aftnos.aftourismserver.portal.vo.NewsPortalVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户新闻接口
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/news")
public class NewsPortalController {

    private final NewsPortalService newsPortalService;

    /**
     * 门户分页查询新闻
     */
    @GetMapping("/page")
    public Result<PageInfo<NewsPortalVO>> page(@Valid NewsPortalPageQuery query) {
        log.info("【门户-分页查询新闻】收到请求，页码={}，每页条数={}，关键词={}", query.getPageNum(), query.getPageSize(), query.getKeyword());
        PageInfo<NewsPortalVO> pageInfo = newsPortalService.pageNews(query);
        return Result.success(pageInfo);
    }

    /**
     * 门户新闻详情
     */
    @GetMapping("/{id}")
    public Result<NewsPortalVO> detail(@PathVariable Long id) {
        log.info("【门户-新闻详情】收到请求，新闻ID={}", id);
        NewsPortalVO detail = newsPortalService.getNewsDetail(id);
        return Result.success(detail);
    }
}
