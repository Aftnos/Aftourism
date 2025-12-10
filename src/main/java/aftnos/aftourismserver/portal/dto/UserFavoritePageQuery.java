package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 用户收藏分页查询参数
 */
@Data
public class UserFavoritePageQuery {

    /** 页码，默认第一页 */
    @Min(value = 1, message = "页码不能小于1")
    private Integer current = 1;

    /** 每页条数，设置合理上限避免过载 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer size = 10;

    /** 收藏类型过滤，可为空表示全部 */
    private String type;
}
