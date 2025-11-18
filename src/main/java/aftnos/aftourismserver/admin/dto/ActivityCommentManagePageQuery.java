package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 活动留言管理分页查询参数
 */
@Data
public class ActivityCommentManagePageQuery {

    @Min(value = 1, message = "页码至少为1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数至少为1")
    @Max(value = 100, message = "每页条数不超过100")
    private Integer pageSize = 10;

    /** 指定父级留言ID，默认查询一级留言 */
    private Long parentId;
}
