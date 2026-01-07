package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户通知视图对象
 */
@Data
public class PortalNotificationVO {
    private Long id;
    private Long userId;
    private String type;
    private String typeText;
    private String title;
    private String content;
    private String relatedType;
    private Long relatedId;
    private Integer isRead;
    private LocalDateTime createTime;
}
