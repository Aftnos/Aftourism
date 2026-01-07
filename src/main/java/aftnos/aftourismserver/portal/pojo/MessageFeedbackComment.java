package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言反馈评论实体，映射 t_message_feedback_comment 表
 */
@Data
public class MessageFeedbackComment {
    private Long id;
    /** 留言反馈ID */
    private Long feedbackId;
    /** 留言用户ID */
    private Long userId;
    /** 留言内容 */
    private String content;
    /** 父留言ID */
    private Long parentId;
    /** 点赞数 */
    private Integer likeCount;
    /** 逻辑删除标记 */
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
