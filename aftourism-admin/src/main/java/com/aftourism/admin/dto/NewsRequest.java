package com.aftourism.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻新增或更新请求。
 */
@Data
public class NewsRequest {

    /** 新闻标题 */
    @NotBlank(message = "标题不能为空")
    private String title;

    /** 新闻内容 */
    @NotBlank(message = "内容不能为空")
    private String content;

    /** 封面 */
    private String coverUrl;

    /** 发布时间 */
    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime;

    /** 作者 */
    private String author;

    /** 状态 */
    private Integer status;
}
