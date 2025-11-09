package com.aftourism.portal.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动申报请求。
 */
@Data
public class ActivityApplyRequest {

    @NotBlank(message = "活动名称不能为空")
    private String name;

    private String coverUrl;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Future(message = "结束时间需晚于当前时间")
    private LocalDateTime endTime;

    private String category;

    @NotNull(message = "请选择场馆")
    private Long venueId;

    private String organizer;
    private String contactPhone;
    private String intro;
}
