package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 交流文章审核参数
 */
@Data
public class ExchangeArticleAuditDTO {
    /** 状态：0待审核 1已发布 2已驳回 */
    @NotNull(message = "审核状态不能为空")
    private Integer status;

    /** 审核备注 */
    @Size(max = 200, message = "审核备注不能超过200字")
    private String auditRemark;
}
