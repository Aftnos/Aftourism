package com.aftourism.portal.service;

import com.aftourism.portal.vo.NewsSummaryVO;

import java.util.List;

/**
 * 门户新闻服务。
 */
public interface NewsPortalService {

    List<NewsSummaryVO> listLatest();
}
