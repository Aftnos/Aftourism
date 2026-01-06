package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 门户资质申请请求参数。
 */
@Data
public class QualificationApplyRequest {

    /** 申请人姓名 */
    @NotBlank(message = "申请人姓名不能为空")
    @Size(max = 50, message = "申请人姓名长度不能超过50个字符")
    private String realName;

    /** 单位/机构名称 */
    @NotBlank(message = "单位/机构名称不能为空")
    @Size(max = 100, message = "单位名称长度不能超过100个字符")
    private String organization;

    /** 联系电话 */
    @NotBlank(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String contactPhone;

    /** 资质申请说明 */
    @NotBlank(message = "资质申请说明不能为空")
    @Size(max = 500, message = "申请说明长度不能超过500个字符")
    private String applyReason;

    /** 资质附件链接 */
    @Size(max = 255, message = "附件链接长度不能超过255个字符")
    private String attachmentUrl;
}
