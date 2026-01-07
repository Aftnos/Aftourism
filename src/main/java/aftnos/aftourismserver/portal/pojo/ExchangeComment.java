package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交流评论实体，映射 t_exchange_comment 表
 */
@Data
public class ExchangeComment {
    private Long id;
    /** 文章ID */
    private Long articleId;
    /** 评论用户ID */
    private Long userId;
    /** 评论内容 */
    private String content;
    /** 父级评论ID */
    private Long parentId;
    /** 被@用户ID */
    private Long mentionUserId;
    /** 点赞数 */
    private Integer likeCount;
    /** 逻辑删除标记 */
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
