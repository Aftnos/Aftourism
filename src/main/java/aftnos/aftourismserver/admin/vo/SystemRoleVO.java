package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色列表展示对象，对齐前端 SystemManage.RoleListItem 结构。
 */
@Data
public class SystemRoleVO {

    /** 角色主键，来源于聚合后的最小 ID */
    private Long roleId;

    /** 角色名称，后端以角色编码美化生成 */
    private String roleName;

    /** 角色编码 */
    private String roleCode;

    /** 描述信息 */
    private String description;

    /** 是否启用，暂无禁用能力默认 true */
    private Boolean enabled;

    /** 创建时间 */
    private LocalDateTime createTime;
}
