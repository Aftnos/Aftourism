package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 单个权限配置项。
 */
@Data
public class RolePermissionItem {

    /** 资源键，例如 NEWS */
    @NotBlank(message = "资源键不能为空")
    @Size(max = 100, message = "资源键长度不能超过 100 字符")
    private String resourceKey;

    /** 动作键，例如 CREATE */
    @NotBlank(message = "动作键不能为空")
    @Size(max = 100, message = "动作键长度不能超过 100 字符")
    private String action;

    /** 是否允许 */
    private Boolean allow = Boolean.TRUE;

    /** 备注 */
    @Size(max = 255, message = "备注长度不能超过 255 字符")
    private String remark;
}
