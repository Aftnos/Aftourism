package aftnos.aftourismserver.admin.dto;

import lombok.Data;

/**
 * 门户用户状态调整请求。
 */
@Data
public class PortalUserUpdateRequest {

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 是否高级用户：1是 0否 */
    private Integer isAdvanced;
}
