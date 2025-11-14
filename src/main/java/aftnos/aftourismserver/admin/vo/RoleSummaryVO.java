package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * 角色权限汇总展示。
 */
@Data
public class RoleSummaryVO {

    private String roleCode;
    private List<RolePermissionVO> permissions;
}
