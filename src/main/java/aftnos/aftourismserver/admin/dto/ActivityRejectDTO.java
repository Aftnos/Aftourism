package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 活动驳回请求参数
 */
@Data
public class ActivityRejectDTO {

    /** 驳回原因说明 */
    @NotBlank(message = "驳回原因不能为空")
    private String rejectReason;
}
