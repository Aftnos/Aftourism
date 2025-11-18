package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动审核列表项
 */
@Data
public class ActivityAuditItemVO {
    private Long id;
    private String name;
    private String category;
    private Long venueId;
    private String venueName;
    private String addressCache;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;
    /** 审核状态：0待审核 1已通过 2已拒绝 */
    private Integer applyStatus;
    /** 上下线状态：0下线 1上线 */
    private Integer onlineStatus;
}