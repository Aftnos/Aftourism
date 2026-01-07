package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    /** 被@用户ID */
    private Long mentionUserId;

    /** 被@用户昵称 */
    private String mentionUserNickname;

    /** 被@用户头像 */
    private String mentionUserAvatar;

    /** 点赞数 */
    private Integer likeCount;

    /** 创建时间 */
    private LocalDateTime createTime;

    /**
     * 子留言集合，实现楼中楼展示
     */
    private List<ActivityCommentVO> children;
}
