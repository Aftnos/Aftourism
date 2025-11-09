package com.aftourism.portal.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

/**
 * 活动留言实体。
 */
@Data
public class ActivityComment extends BaseEntity {

    private Long activityId;
    private Long userId;
    private String content;
    private Long parentId;
    private Integer likeCount;
}
