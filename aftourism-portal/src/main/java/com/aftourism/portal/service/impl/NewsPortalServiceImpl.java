package com.aftourism.portal.service.impl;

import com.aftourism.common.util.BeanCopyUtils;
import com.aftourism.portal.mapper.NewsMapper;
import com.aftourism.portal.pojo.News;
import com.aftourism.portal.service.NewsPortalService;
import com.aftourism.portal.vo.NewsSummaryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新闻服务实现。
 */
@Service
@RequiredArgsConstructor
public class NewsPortalServiceImpl implements NewsPortalService {

    private final NewsMapper newsMapper;

    @Override
    public List<NewsSummaryVO> listLatest() {
        List<News> newsList = newsMapper.selectLatest(10);
        return BeanCopyUtils.copyList(newsList, NewsSummaryVO::new);
    }
}
