package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 通知公告分页查询参数
 */
@Data
public class NoticePageQuery {
    /** 当前页，从 1 开始 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer pageSize = 10;

    /** 标题模糊匹配 */
    private String title;

    /** 状态筛选 */
    private Integer status;
}
