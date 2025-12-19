package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.VenueMapper;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.cache.PortalCacheable;
import aftnos.aftourismserver.portal.dto.VenuePortalPageQuery;
import aftnos.aftourismserver.portal.service.VenuePortalService;
import aftnos.aftourismserver.portal.vo.VenueDetailVO;
import aftnos.aftourismserver.portal.vo.VenueSummaryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门户场馆业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VenuePortalServiceImpl implements VenuePortalService {

    private final VenueMapper venueMapper;

    @Override
    @PortalCacheable(cacheName = "portal:venue:page")
    public PageInfo<VenueSummaryVO> pageVenues(VenuePortalPageQuery query) {
        log.info("【门户-分页查询场馆】开始处理，页码={}，每页条数={}", query.getCurrent(), query.getSize());
        int pageNum = query.getCurrent() != null ? query.getCurrent() : 1;
        int pageSize = query.getSize() != null ? query.getSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<VenueSummaryVO> list = venueMapper.portalPageList(query);
        PageInfo<VenueSummaryVO> pageInfo = new PageInfo<>(list);
        log.info("【门户-分页查询场馆】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    @PortalCacheable(cacheName = "portal:venue:detail", ttlSeconds = 90)
    public VenueDetailVO getDetail(Long id) {
        log.info("【门户-场馆详情】开始处理，场馆ID={}", id);
        VenueDetailVO detail = venueMapper.portalDetail(id);
        if (detail == null) {
            log.warn("【门户-场馆详情】目标不存在或已删除，场馆ID={}", id);
            throw new BusinessException("场馆不存在或已被删除");
        }
        // 中文注释：记录场馆浏览量，供后台点击量统计使用
        venueMapper.incrementViewCount(id, LocalDateTime.now());
        if (detail.getViewCount() != null) {
            detail.setViewCount(detail.getViewCount() + 1);
        } else {
            detail.setViewCount(1L);
        }
        return detail;
    }
}
