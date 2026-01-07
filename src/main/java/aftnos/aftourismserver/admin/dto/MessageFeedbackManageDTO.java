package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 留言反馈管理更新参数
 */
@Data
public class MessageFeedbackManageDTO {
    /** 标题 */
    @Size(max = 100, message = "标题不能超过100字")
    private String title;

    /** 内容 */
    @Size(max = 1000, message = "内容不能超过1000字")
    private String content;

    /** 联系电话 */
    @Size(max = 50, message = "联系电话不能超过50字")
    private String contactPhone;

    /** 联系邮箱 */
    @Size(max = 100, message = "联系邮箱不能超过100字")
    private String contactEmail;

    /** 反馈状态 */
    @NotNull(message = "反馈状态不能为空")
    private Integer status;
}
