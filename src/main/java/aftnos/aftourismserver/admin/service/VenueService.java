package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.VenueDTO;
import aftnos.aftourismserver.admin.dto.VenuePageQuery;
import aftnos.aftourismserver.admin.vo.VenueVO;
import com.github.pagehelper.PageInfo;

/**
 * 场馆后台业务接口
 */
public interface VenueService {

    /** 新增场馆 */
    Long createVenue(VenueDTO venueDTO);

    /** 修改场馆 */
    void updateVenue(Long id, VenueDTO venueDTO);

    /** 删除场馆 */
    void deleteVenue(Long id);

    /** 分页查询场馆 */
    PageInfo<VenueVO> pageVenue(VenuePageQuery query);

    /** 查询场馆详情 */
    VenueVO getVenueDetail(Long id);
}
