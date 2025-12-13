package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.dto.ActivityManagePageQuery;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.vo.ActivityManageDetailVO;
import aftnos.aftourismserver.admin.vo.ActivityManageVO;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.portal.dto.ActivityPortalPageQuery;
import aftnos.aftourismserver.portal.vo.ActivityDetailVO;
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

    /** 门户详情查询 */
    ActivityDetailVO selectPortalDetail(@Param("id") Long id,
                                        @Param("onlineStatus") Integer onlineStatus);

    /** 浏览量自增 */
    int incrementViewCount(@Param("id") Long id);

    /** 门户分页查询 */
    List<ActivitySummaryVO> portalPageList(@Param("query") ActivityPortalPageQuery query,
                                           @Param("onlineStatus") Integer onlineStatus);

    /**
     * 根据ID集合查询活动
     */
    List<Activity> selectByIds(@Param("ids") List<Long> ids);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);

    /** 后台活动管理分页 */
    List<ActivityManageVO> adminManagePageList(@Param("query") ActivityManagePageQuery query);

    /** 活动管理详情 */
    ActivityManageDetailVO selectManageDetail(@Param("id") Long id);
}
