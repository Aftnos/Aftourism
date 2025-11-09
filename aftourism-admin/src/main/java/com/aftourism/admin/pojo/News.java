package com.aftourism.admin.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻实体，对应 t_news 表。
 */
@Data
public class News extends BaseEntity {

    /** 新闻标题 */
    private String title;

    /** 新闻内容 */
    private String content;

    /** 封面图地址 */
    private String coverUrl;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 作者 */
    private String author;

    /** 状态 */
    private Integer status;

    /** 浏览量 */
    private Long viewCount;
}
