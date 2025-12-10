package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.NewsDTO;
import aftnos.aftourismserver.admin.dto.NewsPageQuery;
import aftnos.aftourismserver.admin.service.NewsService;
import aftnos.aftourismserver.admin.vo.NewsVO;
import aftnos.aftourismserver.common.result.Result;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻管理
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/news")
public class NewsController {

    private final NewsService newsService;

    /**
     * 新增新闻
     */
    @PostMapping
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NEWS_CREATE)")
    public Result<Long> create(@Valid @RequestBody NewsDTO newsDTO) {
        log.info("【新增新闻】收到请求，标题={}", newsDTO.getTitle());
        Long newsId = newsService.createNews(newsDTO);
        return Result.success(newsId);
    }

    /**
     * 修改新闻
     */
    @PutMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NEWS_UPDATE)")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody NewsDTO newsDTO) {
        log.info("【修改新闻】收到请求，新闻ID={}", id);
        newsService.updateNews(id, newsDTO);
        return Result.success();
    }

    /**
     * 逻辑删除新闻
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NEWS_DELETE)")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("【删除新闻】收到请求，新闻ID={}", id);
        newsService.deleteNews(id);
        return Result.success();
    }

    /**
     * 分页查询新闻
     * 示例：GET /admin/news/page?current=1&size=10
     * SQL 片段：SELECT ... FROM t_news WHERE is_deleted = 0 ORDER BY update_time DESC
     */
    @GetMapping("/page")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).NEWS_READ)")
    public Result<PageInfo<NewsVO>> page(@Valid NewsPageQuery query) {
        log.info("【分页查询新闻】收到请求，页码={}，每页条数={}", query.getCurrent(), query.getSize());
        PageInfo<NewsVO> pageInfo = newsService.pageNews(query);
        return Result.success(pageInfo);
    }
}
