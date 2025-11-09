package com.aftourism.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 活动留言请求。
 */
@Data
public class CommentRequest {

    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @NotBlank(message = "留言内容不能为空")
    private String content;

    private Long parentId;
}
