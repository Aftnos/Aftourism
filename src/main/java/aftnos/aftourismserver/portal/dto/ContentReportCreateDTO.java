package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 举报提交参数
 */
@Data
public class ContentReportCreateDTO {
    /** 举报目标类型 */
    @NotBlank(message = "举报类型不能为空")
    private String targetType;

    /** 举报目标ID */
    @NotNull(message = "举报目标不能为空")
    private Long targetId;

    /** 举报原因类型 */
    @NotBlank(message = "举报原因类型不能为空")
    private String reasonType;

    /** 举报原因描述 */
    @Size(max = 500, message = "举报原因不能超过500字")
    private String reason;

    /** 截图地址列表 */
    private List<String> screenshotUrls;
}
