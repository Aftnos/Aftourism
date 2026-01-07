package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 门户留言反馈创建参数
 */
@Data
public class MessageFeedbackCreateDTO {
    /** 标题 */
    @Size(max = 100, message = "标题不能超过100字")
    private String title;

    /** 内容 */
    @NotBlank(message = "内容不能为空")
    @Size(max = 1000, message = "内容不能超过1000字")
    private String content;

    /** 联系电话 */
    @Size(max = 50, message = "联系电话不能超过50字")
    private String contactPhone;

    /** 联系邮箱 */
    @Size(max = 100, message = "联系邮箱不能超过100字")
    private String contactEmail;
}
