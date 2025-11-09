package com.aftourism.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 新闻列表展示对象。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsSummaryVO {

    private Long id;
    private String title;
    private String coverUrl;
    private LocalDateTime publishTime;
    private Long viewCount;
}
