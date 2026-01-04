package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 场馆分页查询参数
 */
@Data
public class VenuePageQuery {

    /** 页码，默认1 */
    @Min(value = 1, message = "页码不能小于1")
    private Integer current = 1;

    /** 每页条数，默认10 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 1000, message = "每页条数不能超过1000")
    private Integer size = 10;

    /** 场馆名称模糊查询 */
    private String name;

    /** 场馆类别过滤 */
    private String category;

    /** 地址模糊匹配 */
    private String address;

    /** 是否免费筛选 */
    @PositiveOrZero(message = "是否免费筛选参数非法")
    private Integer isFree;

    /** 类型标签过滤 */
    private String tags;

    /** 省份过滤 */
    private String province;

    /** 城市过滤 */
    private String city;

    /** 区县过滤 */
    private String district;
}
