package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.dto.ActivityAuditPageQuery;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
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

    /** 后台审核分页查询 */
    List<aftnos.aftourismserver.admin.vo.ActivityAuditItemVO> adminAuditPageList(@Param("query") ActivityAuditPageQuery query,
                                                                                 @Param("applyStatus") Integer applyStatus,
                                                                                 @Param("organizer") String organizer);

    /**
     * 根据ID集合查询活动
     */
    List<Activity> selectByIds(@Param("ids") List<Long> ids);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);
}
