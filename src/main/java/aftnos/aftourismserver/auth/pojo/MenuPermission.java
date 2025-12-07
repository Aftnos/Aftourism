package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单按钮/操作权限实体，对应 t_menu_permission。
 */
@Data
public class MenuPermission {

    /** 主键ID */
    private Long id;
    /** 关联的菜单ID */
    private Long menuId;
    /** 权限名称，用于前端展示 */
    private String title;
    /** 权限标识，与前端 authMark 对齐 */
    private String authMark;
    /** 排序值 */
    private Integer sort;
    /** 备注 */
    private String remark;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
