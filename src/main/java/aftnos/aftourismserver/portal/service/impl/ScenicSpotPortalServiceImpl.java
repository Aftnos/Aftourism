package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.ScenicSpotMapper;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ScenicSpotPortalPageQuery;
import aftnos.aftourismserver.portal.service.ScenicSpotPortalService;
import aftnos.aftourismserver.portal.vo.ScenicSpotDetailVO;
import aftnos.aftourismserver.portal.vo.ScenicSpotSummaryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门户景区业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScenicSpotPortalServiceImpl implements ScenicSpotPortalService {

    private final ScenicSpotMapper scenicSpotMapper;

    @Override
    public PageInfo<ScenicSpotSummaryVO> pageScenicSpots(ScenicSpotPortalPageQuery query) {
        log.info("【门户-分页查询景区】开始处理，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<ScenicSpotSummaryVO> list = scenicSpotMapper.portalPageList(query);
        PageInfo<ScenicSpotSummaryVO> pageInfo = new PageInfo<>(list);
        log.info("【门户-分页查询景区】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public ScenicSpotDetailVO getDetail(Long id) {
        log.info("【门户-景区详情】开始处理，景区ID={}", id);
        ScenicSpotDetailVO detail = scenicSpotMapper.portalDetail(id);
        if (detail == null) {
            log.warn("【门户-景区详情】目标景区不存在或已删除，景区ID={}", id);
            throw new BusinessException("景区不存在或已被删除");
        }
        return detail;
    }
}
