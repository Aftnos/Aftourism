package aftnos.aftourismserver.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 权限点枚举展示。
 */
@Data
@AllArgsConstructor
public class PermissionDefinitionVO {

    private String key;
    private String resourceKey;
    private String action;
    private String description;
}
