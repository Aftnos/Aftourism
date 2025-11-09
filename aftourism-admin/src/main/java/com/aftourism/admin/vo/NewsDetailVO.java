package com.aftourism.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 新闻详情返回对象。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDetailVO {

    private Long id;
    private String title;
    private String content;
    private String coverUrl;
    private LocalDateTime publishTime;
    private String author;
    private Integer status;
    private Long viewCount;
}
