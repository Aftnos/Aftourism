package aftnos.aftourismserver.portal.mapper;

import aftnos.aftourismserver.admin.vo.MessageFeedbackManageVO;
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

    List<MessageFeedbackVO> pageList(@Param("type") String type);

    List<MessageFeedbackManageVO> pageListForManage(@Param("type") String type,
                                                    @Param("status") Integer status,
                                                    @Param("keyword") String keyword);

    MessageFeedbackManageVO selectManageById(@Param("id") Long id);

    int updateForManage(@Param("id") Long id,
                        @Param("type") String type,
                        @Param("title") String title,
                        @Param("content") String content,
                        @Param("contactPhone") String contactPhone,
                        @Param("contactEmail") String contactEmail,
                        @Param("status") Integer status,
                        @Param("updateTime") LocalDateTime updateTime);

    int markDeleted(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);
}
