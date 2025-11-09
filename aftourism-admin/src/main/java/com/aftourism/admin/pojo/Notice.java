package com.aftourism.admin.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告实体，对应 t_notice 表。
 */
@Data
public class Notice extends BaseEntity {

    /** 通知标题 */
    private String title;

    /** 通知内容 */
    private String content;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 发布人 */
    private String author;

    /** 状态 */
    private Integer status;

    /** 浏览量 */
    private Long viewCount;
}
