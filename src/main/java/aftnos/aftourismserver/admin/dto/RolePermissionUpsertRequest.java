package aftnos.aftourismserver.admin.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 角色权限批量保存请求。
 */
@Data
public class RolePermissionUpsertRequest {

    /** 角色编码 */
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 100, message = "角色编码长度不能超过 100 字符")
    private String roleCode;

    /** 权限项列表 */
    @NotEmpty(message = "权限列表不能为空")
    @Valid
    private List<RolePermissionItem> permissions;
}
