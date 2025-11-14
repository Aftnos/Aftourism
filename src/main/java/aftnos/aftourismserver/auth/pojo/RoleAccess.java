package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色权限映射实体，对应数据表 t_role_access。
 */
@Data
public class RoleAccess {

    /** 主键ID */
    private Long id;
    /** 角色编码，例如 ADMIN/SUPER_ADMIN */
    private String roleCode;
    /** 资源键，例如 NEWS/NOTICE/SCENIC */
    private String resourceKey;
    /** 动作键，例如 CREATE/READ/UPDATE */
    private String action;
    /** 是否允许：1允许 0拒绝 */
    private Integer allow;
    /** 备注说明 */
    private String remark;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
