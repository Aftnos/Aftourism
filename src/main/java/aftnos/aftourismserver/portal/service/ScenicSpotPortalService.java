package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.ScenicSpotPortalPageQuery;
import aftnos.aftourismserver.portal.vo.ScenicSpotDetailVO;
import aftnos.aftourismserver.portal.vo.ScenicSpotSummaryVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户景区业务接口
 */
public interface ScenicSpotPortalService {

    /**
     * 分页查询景区列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageInfo<ScenicSpotSummaryVO> pageScenicSpots(ScenicSpotPortalPageQuery query);

    /**
     * 根据ID获取景区详情
     *
     * @param id 景区ID
     * @return 详情数据
     */
    ScenicSpotDetailVO getDetail(Long id);
}
