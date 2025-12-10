package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.dto.ActivityAuditPageQuery;
import aftnos.aftourismserver.admin.pojo.ActivityApply;
import aftnos.aftourismserver.admin.vo.ActivityAuditDetailVO;
import aftnos.aftourismserver.admin.vo.ActivityAuditItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台管理模块的活动申请 Mapper，负责活动申请记录的增删改查操作。
 */
@Mapper
public interface ActivityApplyMapper {
    int insert(ActivityApply apply);

    int update(ActivityApply apply);

    ActivityApply selectById(@Param("id") Long id);

    int countOverlapActivities(@Param("venueId") Long venueId,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);

    List<ActivityAuditItemVO> adminAuditPageList(@Param("query") ActivityAuditPageQuery query,
                                                 @Param("applyStatus") Integer applyStatus,
                                                 @Param("organizer") String organizer);

    ActivityAuditDetailVO selectDetailById(@Param("id") Long id);
}
