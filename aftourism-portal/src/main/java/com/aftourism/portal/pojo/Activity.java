package com.aftourism.portal.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动实体，对应 t_activity 表。
 */
@Data
public class Activity extends BaseEntity {

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
    private Integer onlineStatus;
    private Long viewCount;
    private Long favoriteCount;
}
