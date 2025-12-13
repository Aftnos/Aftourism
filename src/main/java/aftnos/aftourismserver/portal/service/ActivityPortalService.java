package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.ActivityApplyDTO;
import aftnos.aftourismserver.portal.dto.ActivityPortalPageQuery;
import aftnos.aftourismserver.portal.vo.ActivityDetailVO;
import aftnos.aftourismserver.portal.vo.ActivitySummaryVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户活动业务接口
 */
public interface ActivityPortalService {

    /**
     * 用户申报活动
     */
    Long apply(ActivityApplyDTO dto, Long userId);

    /**
     * 门户分页查询活动
     */
    PageInfo<ActivitySummaryVO> pageActivities(ActivityPortalPageQuery query);

    /**
     * 门户活动详情
     */
    ActivityDetailVO getDetail(Long id);
}
