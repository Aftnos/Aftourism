package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 门户场馆分页查询参数
 */
@Data
public class VenuePortalPageQuery {

    /** 页码 */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 10;

    /** 场馆名称模糊查询 */
    private String name;

    /** 类别筛选 */
    private String category;

    /** 地址模糊匹配 */
    private String address;

    /** 是否免费筛选 */
    @PositiveOrZero(message = "是否免费筛选参数非法")
    private Integer isFree;
}
