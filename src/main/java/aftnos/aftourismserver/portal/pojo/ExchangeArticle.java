package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交流文章实体，映射 t_exchange_article 表
 */
@Data
public class ExchangeArticle {
    private Long id;
    /** 作者用户ID */
    private Long userId;
    /** 标题 */
    private String title;
    /** 富文本内容 */
    private String content;
    /** 封面地址 */
    private String coverUrl;
    /** 状态：0待审核 1已发布 2已驳回 */
    private Integer status;
    /** 点赞数 */
    private Integer likeCount;
    /** 评论数 */
    private Integer commentCount;
    /** 审核备注 */
    private String auditRemark;
    /** 逻辑删除标记 */
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
