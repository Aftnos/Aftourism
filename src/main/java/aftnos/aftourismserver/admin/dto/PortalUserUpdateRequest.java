package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 门户用户角色或状态调整请求。
 */
@Data
public class PortalUserUpdateRequest {

    /** 新角色编码 */
    @Size(max = 100, message = "角色编码长度不能超过 100 字符")
    private String roleCode;

    /** 状态：1启用 0禁用 */
    private Integer status;
}
