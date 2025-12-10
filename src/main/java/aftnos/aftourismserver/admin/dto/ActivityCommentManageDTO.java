package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 后台管理创建/更新活动留言的参数对象，支持楼中楼回复。
 */
@Data
public class ActivityCommentManageDTO {

    /** 留言用户ID，后台可指定为任意有效门户用户 */
    @NotNull(message = "留言用户ID不能为空")
    private Long userId;

    /** 留言内容，限制长度避免滥用 */
    @NotBlank(message = "留言内容不能为空")
    @Size(max = 500, message = "留言内容不能超过500字")
    private String content;

    /** 父留言ID，可为空，用于创建一级楼层或回复楼中楼 */
    private Long parentId;
}
