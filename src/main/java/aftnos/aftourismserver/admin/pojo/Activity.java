package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动实体，对应表 t_activity
 */
@Data
public class Activity {

    /** 主键ID */
    private Long id;
    /** 活动名称 */
    private String name;
    /** 活动封面图 */
    private String coverUrl;
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
    /** 活动类别 */
    private String category;
    /** 场馆ID */
    private Long venueId;
    /** 主办单位 */
    private String organizer;
    /** 联系电话 */
    private String contactPhone;
    /** 活动简介 */
    private String intro;
    /** 场馆地址快照 */
    private String addressCache;
    /** 申报用户ID */
    private Long applyUserId;
    /** 审核状态 */
    private Integer applyStatus;
    /** 驳回原因 */
    private String rejectReason;
    /** 上线状态 */
    private Integer onlineStatus;
    /** 浏览量 */
    private Long viewCount;
    /** 收藏数量 */
    private Long favoriteCount;
    /** 逻辑删除标识 */
    private Integer isDeleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
