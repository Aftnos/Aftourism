package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 管理员账户分页查询请求。
 */
@Data
public class AdminAccountPageQuery {

    /** 页码，默认第 1 页 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer current = 1;

    /** 每页条数，默认 10，最大 100 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100 条")
    private Integer size = 10;

    /** 按账号模糊查询 */
    private String username;

    /** 按姓名模糊查询 */
    private String realName;

    /** 状态筛选 */
    @PositiveOrZero(message = "状态值需为非负数字")
    private Integer status;
}
