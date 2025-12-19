package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.NoticeMapper;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.cache.PortalCacheable;
import aftnos.aftourismserver.portal.dto.NoticePortalPageQuery;
import aftnos.aftourismserver.portal.service.NoticePortalService;
import aftnos.aftourismserver.portal.vo.NoticeDetailVO;
import aftnos.aftourismserver.portal.vo.NoticeSummaryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门户通知公告业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticePortalServiceImpl implements NoticePortalService {

    private final NoticeMapper noticeMapper;

    @Override
    @PortalCacheable(cacheName = "portal:notice:page")
    public PageInfo<NoticeSummaryVO> pageNotices(NoticePortalPageQuery query) {
        log.info("【门户-分页查询通知】开始处理，页码={}，每页条数={}", query.getCurrent(), query.getSize());
        int pageNum = query.getCurrent() != null ? query.getCurrent() : 1;
        int pageSize = query.getSize() != null ? query.getSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<NoticeSummaryVO> list = noticeMapper.portalPageList(query);
        PageInfo<NoticeSummaryVO> pageInfo = new PageInfo<>(list);
        log.info("【门户-分页查询通知】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    @PortalCacheable(cacheName = "portal:notice:detail", ttlSeconds = 90)
    public NoticeDetailVO getNoticeDetail(Long id) {
        log.info("【门户-通知详情】开始处理，通知ID={}", id);
        NoticeDetailVO detail = noticeMapper.portalDetail(id);
        if (detail == null) {
            log.warn("【门户-通知详情】目标不存在或已删除，通知ID={}", id);
            throw new BusinessException("通知不存在或已被删除");
        }
        int rows = noticeMapper.incrementViewCount(id, LocalDateTime.now());
        if (rows == 0) {
            log.warn("【门户-通知详情】浏览量自增失败，通知ID={}", id);
        } else if (detail.getViewCount() != null) {
            detail.setViewCount(detail.getViewCount() + 1);
        } else {
            detail.setViewCount(1L);
        }
        return detail;
    }
}
