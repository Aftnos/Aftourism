package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户通知实体，映射 t_portal_notification 表
 */
@Data
public class PortalNotification {
    private Long id;
    /** 接收用户ID */
    private Long userId;
    /** 通知类型 */
    private String type;
    /** 通知标题 */
    private String title;
    /** 通知内容 */
    private String content;
    /** 关联类型 */
    private String relatedType;
    /** 关联ID */
    private Long relatedId;
    /** 已读状态：0未读 1已读 */
    private Integer isRead;
    /** 逻辑删除标记 */
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
