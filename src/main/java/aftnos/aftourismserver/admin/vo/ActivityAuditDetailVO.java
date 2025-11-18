package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动审核详情
 */
@Data
public class ActivityAuditDetailVO {
    private Long id;
    private String name;
    private String coverUrl;
    private String category;
    private Long venueId;
    private String venueName;
    private String addressCache;
    private String organizer;
    private String contactPhone;
    private String intro;
    private Long applyUserId;
    private Integer applyStatus;
    private String rejectReason;
    private String auditRemark;
    private Long activityId;
    private Integer onlineStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;
}
