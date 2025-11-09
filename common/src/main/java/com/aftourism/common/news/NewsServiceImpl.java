package com.aftourism.common.news;

import com.aftourism.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Default implementation using MyBatis mapper and PageHelper for pagination.
 */
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;

    @Override
    public Long createNews(News news) {
        news.setIsDeleted(false);
        LocalDateTime now = LocalDateTime.now();
        news.setCreateTime(now);
        news.setUpdateTime(now);
        if (news.getPublishTime() == null) {
            news.setPublishTime(now);
        }
        newsMapper.insert(news);
        return news.getId();
    }

    @Override
    public void updateNews(News news) {
        if (news.getId() == null) {
            throw new BusinessException("News id is required for update");
        }
        news.setUpdateTime(LocalDateTime.now());
        int updated = newsMapper.update(news);
        if (updated == 0) {
            throw new BusinessException("News not found or already deleted");
        }
    }

    @Override
    public void deleteNews(Long id) {
        int deleted = newsMapper.logicDelete(id);
        if (deleted == 0) {
            throw new BusinessException("News not found or already deleted");
        }
    }

    @Override
    public News findById(Long id) {
        return newsMapper.selectById(id);
    }

    @Override
    public PageInfo<News> pageQuery(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsMapper.selectAll();
        return new PageInfo<>(list);
    }

    @Override
    public List<News> latest(int size) {
        PageHelper.startPage(1, size);
        return newsMapper.selectAll();
    }
}
