package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 景区后台分页查询参数
 */
@Data
public class ScenicSpotPageQuery {
    /** 页码，默认从1开始 */
    @Min(value = 1, message = "页码至少为1")
    private Integer current = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数至少为1")
    @Max(value = 1000, message = "每页条数不能超过1000")
    private Integer size = 10;

    /** 名称模糊搜索 */
    private String name;

    /** 地址模糊搜索 */
    private String address;
}
