package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 举报处理参数
 */
@Data
public class ContentReportManageDTO {
    /** 处理状态 */
    @NotNull(message = "处理状态不能为空")
    private Integer status;

    /** 是否违规 */
    private Integer violationFlag;

    /** 处理结果说明 */
    @Size(max = 200, message = "处理说明不能超过200字")
    private String resultRemark;
}
