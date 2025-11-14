package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 门户用户后台分页查询参数。
 */
@Data
public class PortalUserPageQuery {

    @Min(value = 1, message = "页码至少为 1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100 条")
    private Integer pageSize = 10;

    /** 按账号模糊查询 */
    private String username;

    /** 按昵称模糊查询 */
    private String nickname;

    /** 状态筛选 */
    @PositiveOrZero(message = "状态值需为非负数字")
    private Integer status;
}
