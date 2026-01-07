package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.portal.pojo.MessageFeedbackComment;
import aftnos.aftourismserver.portal.vo.MessageFeedbackCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 留言反馈评论数据访问层
 */
@Mapper
public interface MessageFeedbackCommentMapper {

    int insert(MessageFeedbackComment comment);

    MessageFeedbackComment selectById(@Param("id") Long id);

    MessageFeedbackCommentVO selectVOById(@Param("id") Long id);

    List<MessageFeedbackCommentVO> pageList(@Param("feedbackId") Long feedbackId,
                                            @Param("parentId") Long parentId);

    List<MessageFeedbackCommentVO> listByParentId(@Param("parentId") Long parentId);

    List<MessageFeedbackCommentVO> listByParentIds(@Param("parentIds") List<Long> parentIds);

    int markDeleted(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int increaseLikeCount(@Param("id") Long id, @Param("delta") int delta, @Param("updateTime") LocalDateTime updateTime);

    List<Long> selectIdsByParentId(@Param("parentId") Long parentId);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);
}
