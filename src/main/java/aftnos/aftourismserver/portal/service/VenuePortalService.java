package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.VenuePortalPageQuery;
import aftnos.aftourismserver.portal.vo.VenueDetailVO;
import aftnos.aftourismserver.portal.vo.VenueSummaryVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户场馆业务接口
 */
public interface VenuePortalService {

    /** 门户分页查询场馆 */
    PageInfo<VenueSummaryVO> pageVenues(VenuePortalPageQuery query);

    /** 门户查看场馆详情 */
    VenueDetailVO getDetail(Long id);
}
