package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 门户新闻分页查询参数
 */
@Data
public class NewsPortalPageQuery {

    /** 当前页码，从 1 开始 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer pageNum = 1;

    /** 每页展示条数 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer pageSize = 10;

    /** 标题关键词，支持模糊匹配 */
    private String keyword;
}
