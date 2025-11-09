package com.aftourism.admin.service.impl;

import com.aftourism.admin.dto.NewsRequest;
import com.aftourism.admin.mapper.NewsMapper;
import com.aftourism.admin.pojo.News;
import com.aftourism.admin.service.NewsService;
import com.aftourism.admin.vo.NewsDetailVO;
import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResultCode;
import com.aftourism.common.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新闻管理服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;

    @Override
    public Long createNews(NewsRequest request) {
        News news = BeanCopyUtils.copy(request, News::new);
        news.setViewCount(0L);
        newsMapper.insert(news);
        log.info("新增新闻成功，ID:{}", news.getId());
        return news.getId();
    }

    @Override
    public void updateNews(Long id, NewsRequest request) {
        News dbNews = newsMapper.selectById(id);
        if (dbNews == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "新闻不存在");
        }
        News news = BeanCopyUtils.copy(request, News::new);
        news.setId(id);
        newsMapper.update(news);
    }

    @Override
    public void deleteNews(Long id) {
        newsMapper.delete(id);
    }

    @Override
    public List<NewsDetailVO> listNews() {
        List<News> newsList = newsMapper.selectAll();
        return BeanCopyUtils.copyList(newsList, NewsDetailVO::new);
    }
}
