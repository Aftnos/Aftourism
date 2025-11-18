package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动管理列表视图
 */
@Data
public class ActivityManageVO {

    private Long id;
    private String name;
    private String coverUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private Long venueId;
    private String venueName;
    private String addressCache;
    private Integer onlineStatus;
    private Long viewCount;
    private Long favoriteCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
