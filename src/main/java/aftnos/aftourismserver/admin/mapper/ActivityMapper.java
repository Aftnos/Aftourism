package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.portal.dto.ActivityPortalPageQuery;
import aftnos.aftourismserver.portal.vo.ActivitySummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动相关数据库操作
 */
@Mapper
public interface ActivityMapper {

    /** 新增活动 */
    int insert(Activity activity);

    /** 更新活动 */
    int update(Activity activity);

    /** 根据ID查询活动 */
    Activity selectById(@Param("id") Long id);

    /** 统计同一场馆时间冲突的活动数量 */
    int countOverlapActivities(@Param("venueId") Long venueId,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);

    /** 门户分页查询 */
    List<ActivitySummaryVO> portalPageList(@Param("query") ActivityPortalPageQuery query,
                                           @Param("applyStatus") Integer applyStatus,
                                           @Param("onlineStatus") Integer onlineStatus);
}
