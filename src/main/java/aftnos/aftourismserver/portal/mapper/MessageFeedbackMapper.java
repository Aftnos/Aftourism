package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.admin.vo.MessageFeedbackManageVO;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.portal.pojo.MessageFeedback;
import aftnos.aftourismserver.portal.vo.MessageFeedbackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 留言反馈数据访问层
 */
@Mapper
public interface MessageFeedbackMapper {

    int insert(MessageFeedback feedback);

    MessageFeedback selectById(@Param("id") Long id);

    MessageFeedbackVO selectVOById(@Param("id") Long id);

    List<MessageFeedbackVO> pageList();

    List<MessageFeedbackManageVO> pageListForManage(@Param("status") Integer status,
                                                    @Param("keyword") String keyword);

    MessageFeedbackManageVO selectManageById(@Param("id") Long id);

    int updateForManage(@Param("id") Long id,
                        @Param("title") String title,
                        @Param("content") String content,
                        @Param("contactPhone") String contactPhone,
                        @Param("contactEmail") String contactEmail,
                        @Param("status") Integer status,
                        @Param("updateTime") LocalDateTime updateTime);

    int markDeleted(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    List<RecycleItemVO> selectDeletedList(@Param("keyword") String keyword,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int restoreById(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int forceDeleteById(@Param("id") Long id);
}
