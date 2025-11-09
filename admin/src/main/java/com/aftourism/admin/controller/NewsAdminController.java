package com.aftourism.admin.controller;

import com.aftourism.admin.dto.NewsRequest;
import com.aftourism.common.news.News;
import com.aftourism.common.news.NewsService;
import com.aftourism.common.response.ApiResponse;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin controller to manage news CRUD operations.
 */
@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class NewsAdminController {

    private final NewsService newsService;

    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody NewsRequest request) {
        News news = convert(request);
        return ApiResponse.success("Created", newsService.createNews(news));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody NewsRequest request) {
        News news = convert(request);
        news.setId(id);
        newsService.updateNews(news);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<News> findOne(@PathVariable Long id) {
        return ApiResponse.success(newsService.findById(id));
    }

    @GetMapping
    public ApiResponse<PageInfo<News>> page(@RequestParam(defaultValue = "1") @Positive int page,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        return ApiResponse.success(newsService.pageQuery(page, size));
    }

    private News convert(NewsRequest request) {
        News news = new News();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setPublishTime(request.getPublishTime());
        news.setAuthorId(request.getAuthorId());
        return news;
    }
}
