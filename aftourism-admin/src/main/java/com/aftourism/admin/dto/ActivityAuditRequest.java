package com.aftourism.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 活动审核请求。
 */
@Data
public class ActivityAuditRequest {

    /** 活动ID */
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    /** 审核结果 */
    @NotNull(message = "审核结果不能为空")
    private Integer applyStatus;

    /** 驳回原因 */
    private String rejectReason;

    /** 审核备注 */
    private String remark;
}
