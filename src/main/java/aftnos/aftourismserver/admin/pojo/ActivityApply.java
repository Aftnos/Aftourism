package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动申报记录
 */
@Data
public class ActivityApply {
    private Long id;
    private String name;
    private String coverUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private Long venueId;
    private String organizer;
    private String contactPhone;
    private String intro;
    private String addressCache;
    private Long applyUserId;
    private Integer applyStatus;
    private String rejectReason;
    private String auditRemark;
    /** 审核通过后关联的特色活动ID */
    private Long activityId;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
