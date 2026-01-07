package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 门户活动申报查询参数
 */
@Data
public class ActivityApplyPageQuery {

    /** 页码 */
    @Min(value = 1, message = "页码不能小于1")
    private Integer current = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer size = 10;

    /** 申报状态过滤 */
    private Integer applyStatus;

    /** 活动名称关键词 */
    private String name;
}
