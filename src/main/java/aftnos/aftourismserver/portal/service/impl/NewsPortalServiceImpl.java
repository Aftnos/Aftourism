package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.NewsMapper;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.NewsPortalPageQuery;
import aftnos.aftourismserver.portal.service.NewsPortalService;
import aftnos.aftourismserver.portal.vo.NewsPortalVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门户新闻业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsPortalServiceImpl implements NewsPortalService {

    private final NewsMapper newsMapper;

    @Override
    public PageInfo<NewsPortalVO> pageNews(NewsPortalPageQuery query) {
        log.info("【门户-分页查询新闻】开始处理，页码={}，每页条数={}，关键词={}", query.getCurrent(), query.getSize(), query.getKeyword());
        int pageNum = query.getCurrent() != null ? query.getCurrent() : 1;
        int pageSize = query.getSize() != null ? query.getSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<NewsPortalVO> list = newsMapper.portalPageList(query);
        list.forEach(item -> fillSummaryIfBlank(item));
        PageInfo<NewsPortalVO> pageInfo = new PageInfo<>(list);
        log.info("【门户-分页查询新闻】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public NewsPortalVO getNewsDetail(Long id) {
        log.info("【门户-新闻详情】开始处理，新闻ID={}", id);
        NewsPortalVO detail = newsMapper.portalDetail(id);
        if (detail == null) {
            log.warn("【门户-新闻详情】目标新闻不存在或未发布，新闻ID={}", id);
            throw new BusinessException("新闻不存在或已被删除");
        }
        fillSummaryIfBlank(detail);
        int rows = newsMapper.incrementViewCount(id, LocalDateTime.now());
        if (rows == 0) {
            log.warn("【门户-新闻详情】浏览量自增失败，新闻ID={}", id);
        } else if (detail.getViewCount() != null) {
            detail.setViewCount(detail.getViewCount() + 1);
        } else {
            detail.setViewCount(1L);
        }
        return detail;
    }

    /**
     * 当摘要为空时，基于正文自动生成摘要信息
     *
     * @param vo 新闻展示对象
     */
    private void fillSummaryIfBlank(NewsPortalVO vo) {
        if (vo == null) {
            return;
        }
        if ((vo.getSummary() == null || vo.getSummary().isEmpty()) && vo.getContent() != null) {
            String content = vo.getContent().replaceAll("\\s+", " ").trim();
            if (content.length() > 120) {
                vo.setSummary(content.substring(0, 120) + "...");
            } else {
                vo.setSummary(content);
            }
        }
    }
}
