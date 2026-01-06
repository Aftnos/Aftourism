package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 资质审核请求参数。
 */
@Data
public class QualificationAuditRequest {

    /** 审核备注 */
    @Size(max = 255, message = "审核备注长度不能超过255个字符")
    private String auditRemark;
}
