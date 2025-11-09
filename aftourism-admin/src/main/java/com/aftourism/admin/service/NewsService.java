package com.aftourism.admin.service;

import com.aftourism.admin.dto.NewsRequest;
import com.aftourism.admin.vo.NewsDetailVO;

import java.util.List;

/**
 * 新闻管理服务接口。
 */
public interface NewsService {

    /**
     * 新增新闻。
     */
    Long createNews(NewsRequest request);

    /**
     * 更新新闻。
     */
    void updateNews(Long id, NewsRequest request);

    /**
     * 删除新闻。
     */
    void deleteNews(Long id);

    /**
     * 查询新闻列表。
     */
    List<NewsDetailVO> listNews();
}
