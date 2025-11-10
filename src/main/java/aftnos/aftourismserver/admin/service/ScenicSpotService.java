package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ScenicSpotDTO;
import aftnos.aftourismserver.admin.dto.ScenicSpotPageQuery;
import aftnos.aftourismserver.admin.vo.ScenicSpotVO;
import com.github.pagehelper.PageInfo;

/**
 * 景区后台业务接口
 */
public interface ScenicSpotService {

    /**
     * 新增景区
     *
     * @param scenicSpotDTO 景区参数
     * @return 新增后的主键ID
     */
    Long createScenicSpot(ScenicSpotDTO scenicSpotDTO);

    /**
     * 修改景区
     *
     * @param id            景区ID
     * @param scenicSpotDTO 修改参数
     */
    void updateScenicSpot(Long id, ScenicSpotDTO scenicSpotDTO);

    /**
     * 逻辑删除景区
     *
     * @param id 景区ID
     */
    void deleteScenicSpot(Long id);

    /**
     * 分页查询景区
     *
     * @param query 查询条件
     * @return 分页数据
     */
    PageInfo<ScenicSpotVO> pageScenicSpot(ScenicSpotPageQuery query);

    /**
     * 查询景区详情
     *
     * @param id 景区ID
     * @return 景区详情
     */
    ScenicSpotVO getScenicSpotDetail(Long id);
}
