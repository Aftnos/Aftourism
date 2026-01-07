package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户活动申报记录视图
 */
@Data
public class ActivityApplyRecordVO {
    /** 申报ID */
    private Long id;
    /** 活动名称 */
    private String name;
    /** 活动类别 */
    private String category;
    /** 场馆ID */
    private Long venueId;
    /** 场馆名称 */
    private String venueName;
    /** 地址缓存 */
    private String addressCache;
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
    /** 申报状态 */
    private Integer applyStatus;
    /** 申报状态文本 */
    private String applyStatusText;
    /** 审核备注 */
    private String auditRemark;
    /** 驳回原因 */
    private String rejectReason;
    /** 审核通过后生成的活动ID */
    private Long activityId;
    /** 申报时间 */
    private LocalDateTime createTime;
}
