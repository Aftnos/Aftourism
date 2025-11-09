package com.aftourism.portal.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻实体，供门户展示。
 */
@Data
public class News extends BaseEntity {

    private String title;
    private String content;
    private String coverUrl;
    private LocalDateTime publishTime;
    private String author;
    private Integer status;
    private Long viewCount;
}
