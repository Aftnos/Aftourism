package com.aftourism.portal.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告实体。
 */
@Data
public class Notice extends BaseEntity {

    private String title;
    private String content;
    private LocalDateTime publishTime;
    private String author;
    private Integer status;
    private Long viewCount;
}
