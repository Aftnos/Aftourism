package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ActivityManageDTO;
import aftnos.aftourismserver.admin.dto.ActivityManagePageQuery;
import aftnos.aftourismserver.admin.vo.ActivityManageDetailVO;
import aftnos.aftourismserver.admin.vo.ActivityManageVO;
import com.github.pagehelper.PageInfo;

/**
 * 活动管理业务接口
 */
public interface ActivityManageService {

    PageInfo<ActivityManageVO> page(ActivityManagePageQuery query);

    Long create(ActivityManageDTO dto);

    void update(Long id, ActivityManageDTO dto);

    void delete(Long id);

    ActivityManageDetailVO detail(Long id);
}
