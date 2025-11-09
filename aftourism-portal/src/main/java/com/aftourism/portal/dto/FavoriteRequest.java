package com.aftourism.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 收藏请求参数。
 */
@Data
public class FavoriteRequest {

    @NotBlank(message = "收藏类型不能为空")
    private String targetType;

    @NotNull(message = "目标ID不能为空")
    private Long targetId;
}
