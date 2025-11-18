package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动管理详情视图
 */
@Data
public class ActivityManageDetailVO {

    private Long id;
    private String name;
    private String coverUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private Long venueId;
    private String venueName;
    private String addressCache;
    private String organizer;
    private String contactPhone;
    private String intro;
    private Integer onlineStatus;
    private Long viewCount;
    private Long favoriteCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
