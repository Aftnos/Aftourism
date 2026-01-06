package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 资质审核分页查询参数。
 */
@Data
public class QualificationPageQuery {
    /** 当前页，从 1 开始 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer current = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer size = 10;

    /** 申请人账号模糊匹配 */
    private String userName;

    /** 审核状态筛选：0待审核 1已通过 2已驳回 */
    private Integer applyStatus;
}
