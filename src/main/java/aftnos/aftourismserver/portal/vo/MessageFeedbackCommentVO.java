package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 留言反馈评论展示对象
 */
@Data
public class MessageFeedbackCommentVO {
    /** 留言ID */
    private Long id;
    /** 留言反馈ID */
    private Long feedbackId;
    /** 留言用户ID */
    private Long userId;
    /** 留言用户昵称 */
    private String userNickname;
    /** 留言用户头像 */
    private String userAvatar;
    /** 留言内容 */
    private String content;
    /** 父留言ID */
    private Long parentId;
    /** 子留言数量 */
    private Integer childCount;
    /** 点赞数 */
    private Integer likeCount;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 子留言列表 */
    private List<MessageFeedbackCommentVO> children;
}
