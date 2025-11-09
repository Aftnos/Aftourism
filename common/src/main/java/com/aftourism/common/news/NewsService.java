package com.aftourism.common.news;

import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * Service abstraction for news domain.
 */
public interface NewsService {

    Long createNews(News news);

    void updateNews(News news);

    void deleteNews(Long id);

    News findById(Long id);

    PageInfo<News> pageQuery(int pageNum, int pageSize);

    List<News> latest(int size);
}
