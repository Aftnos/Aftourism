package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动留言展示对象
 */
@Data
public class ActivityCommentVO {

    /** 留言ID */
    private Long id;

    /** 活动ID */
    private Long activityId;

    /** 留言用户ID */
    private Long userId;

    /** 用户昵称 */
    private String userNickname;

    /** 用户头像 */
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
}
