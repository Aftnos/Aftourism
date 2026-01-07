package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 留言反馈评论创建参数
 */
@Data
public class MessageFeedbackCommentCreateDTO {
    /** 留言内容 */
    @NotBlank(message = "留言内容不能为空")
    @Size(max = 500, message = "留言内容不能超过500字")
    private String content;

    /** 父留言ID，可为空用于一级留言 */
    private Long parentId;
}
