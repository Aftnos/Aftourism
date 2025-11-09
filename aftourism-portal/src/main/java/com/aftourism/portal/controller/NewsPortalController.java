package com.aftourism.portal.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.portal.service.NewsPortalService;
import com.aftourism.portal.vo.NewsSummaryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 门户新闻接口。
 */
@RestController
@RequestMapping("/portal/news")
@RequiredArgsConstructor
public class NewsPortalController {

    private final NewsPortalService newsPortalService;

    /** 最新新闻列表 */
    @GetMapping("/latest")
    public ResponseResult<List<NewsSummaryVO>> listLatest() {
        return ResponseResult.ok(newsPortalService.listLatest());
    }
}
