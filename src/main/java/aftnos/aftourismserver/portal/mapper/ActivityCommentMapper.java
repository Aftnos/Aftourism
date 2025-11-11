package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.portal.pojo.ActivityComment;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动留言数据访问层
 */
@Mapper
public interface ActivityCommentMapper {

    /** 新增留言 */
    int insert(ActivityComment comment);

    /** 根据ID查询留言 */
    ActivityComment selectById(@Param("id") Long id);

    /**
     * 分页查询留言列表
     */
    List<ActivityCommentVO> pageList(@Param("activityId") Long activityId,
                                     @Param("parentId") Long parentId);
}
