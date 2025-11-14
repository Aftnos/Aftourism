package aftnos.aftourismserver.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 单个角色权限项展示对象。
 */
@Data
@AllArgsConstructor
public class RolePermissionVO {

    private String resourceKey;
    private String action;
    private Boolean allow;
    private String remark;
}
