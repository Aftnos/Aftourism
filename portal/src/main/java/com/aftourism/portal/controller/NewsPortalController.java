package com.aftourism.portal.controller;

import com.aftourism.common.news.News;
import com.aftourism.common.news.NewsService;
import com.aftourism.common.response.ApiResponse;
import com.github.pagehelper.PageInfo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public facing controller exposing read-only news APIs.
 */
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Validated
public class NewsPortalController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    public ApiResponse<News> findById(@PathVariable Long id) {
        return ApiResponse.success(newsService.findById(id));
    }

    @GetMapping("/page")
    public ApiResponse<PageInfo<News>> page(@RequestParam(defaultValue = "1") @Positive int page,
                                            @RequestParam(defaultValue = "10") @Max(100) @Positive int size) {
        return ApiResponse.success(newsService.pageQuery(page, size));
    }

    @GetMapping("/latest")
    public ApiResponse<List<News>> latest(@RequestParam(defaultValue = "5") @Min(1) @Max(20) int size) {
        return ApiResponse.success(newsService.latest(size));
    }
}
