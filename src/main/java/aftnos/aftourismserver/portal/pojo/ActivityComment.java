package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动留言实体，映射 t_activity_comment 表
 */
@Data
public class ActivityComment {

    /** 主键ID */
    private Long id;

    /** 活动ID */
    private Long activityId;

    /** 留言用户ID */
    private Long userId;

    /** 留言内容 */
    private String content;

    /** 父留言ID，可为空实现楼中楼 */
    private Long parentId;

    /** 点赞数量 */
    private Integer likeCount;

    /** 逻辑删除标记：0-正常 1-删除 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
