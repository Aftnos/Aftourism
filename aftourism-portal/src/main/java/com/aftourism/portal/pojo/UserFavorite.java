package com.aftourism.portal.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

/**
 * 用户收藏实体。
 */
@Data
public class UserFavorite extends BaseEntity {

    private Long userId;
    private String targetType;
    private Long targetId;
}
