package com.aftourism.admin.controller;

import com.aftourism.admin.dto.NewsRequest;
import com.aftourism.admin.service.NewsService;
import com.aftourism.admin.vo.NewsDetailVO;
import com.aftourism.common.pojo.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新闻管理接口。
 */
@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /** 新增新闻 */
    @PostMapping
    public ResponseResult<Long> createNews(@Valid @RequestBody NewsRequest request) {
        return ResponseResult.ok(newsService.createNews(request));
    }

    /** 更新新闻 */
    @PutMapping("/{id}")
    public ResponseResult<Void> updateNews(@PathVariable Long id, @Valid @RequestBody NewsRequest request) {
        newsService.updateNews(id, request);
        return ResponseResult.ok();
    }

    /** 删除新闻 */
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseResult.ok();
    }

    /** 新闻列表 */
    @GetMapping
    public ResponseResult<List<NewsDetailVO>> listNews() {
        return ResponseResult.ok(newsService.listNews());
    }
}
