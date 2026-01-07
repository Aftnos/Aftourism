package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 留言反馈管理分页查询参数
 */
@Data
public class MessageFeedbackManagePageQuery {
    /** 页码 */
    @Min(value = 1, message = "页码不能小于1")
    private Integer current = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer size = 10;

    /** 类型过滤 */
    private String type;

    /** 状态过滤 */
    private Integer status;

    /** 关键词搜索（标题/内容/用户） */
    private String keyword;
}
