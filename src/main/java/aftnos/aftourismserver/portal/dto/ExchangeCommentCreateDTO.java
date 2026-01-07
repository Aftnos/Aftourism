package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 交流评论创建参数
 */
@Data
public class ExchangeCommentCreateDTO {
    /** 评论内容 */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论内容不能超过500字")
    private String content;

    /** 父级评论ID */
    private Long parentId;

    /** 被@用户ID */
    private Long mentionUserId;
}
