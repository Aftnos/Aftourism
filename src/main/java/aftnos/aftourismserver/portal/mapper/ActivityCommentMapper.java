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

    /** 根据ID查询留言并补充子楼层数量 */
    ActivityCommentVO selectVOById(@Param("id") Long id);

    /**
     * 分页查询留言列表
     */
    List<ActivityCommentVO> pageList(@Param("activityId") Long activityId,
                                     @Param("parentId") Long parentId);

    /** 查询指定父级下的所有子留言（不分页） */
    List<ActivityCommentVO> listByParentId(@Param("parentId") Long parentId);

    /** 查询指定活动下的全部留言（不分页） */
    List<ActivityCommentVO> listAllByActivity(@Param("activityId") Long activityId);

    /**
     * 标记留言为删除
     */
    int markDeleted(@Param("id") Long id, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 后台管理更新留言的内容、父级及关联用户
     */
    int updateForManage(@Param("id") Long id,
                        @Param("content") String content,
                        @Param("parentId") Long parentId,
                        @Param("userId") Long userId,
                        @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 查询指定父级下的所有留言ID
     */
    List<Long> selectIdsByParentId(@Param("parentId") Long parentId);
}
