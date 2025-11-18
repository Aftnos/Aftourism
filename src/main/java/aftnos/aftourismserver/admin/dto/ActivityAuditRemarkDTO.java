package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 活动审核备注请求
 */
@Data
public class ActivityAuditRemarkDTO {

    /** 审核备注，允许为空字符串以清空 */
    @NotNull(message = "审核备注不能为空")
    @Size(max = 255, message = "审核备注不能超过255个字符")
    private String auditRemark;
}
