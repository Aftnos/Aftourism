package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 门户景区分页查询参数
 */
@Data
public class ScenicSpotPortalPageQuery {
    /** 页码，默认 1 */
    @Min(value = 1, message = "页码至少为1")
    private Integer pageNum = 1;

    /** 每页条数，默认 10 */
    @Min(value = 1, message = "每页条数至少为1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 10;

    /** 名称模糊搜索 */
    private String name;

    /** 地址模糊搜索 */
    private String address;
}
