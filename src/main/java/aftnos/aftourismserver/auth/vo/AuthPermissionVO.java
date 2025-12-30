package aftnos.aftourismserver.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单对应的按钮/权限标识视图对象，对应前端的 authList 元素。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthPermissionVO {
    /** 权限ID */
    private Long id;
    /** 权限展示名称 */
    private String title;
    /** 权限标识，用于前端校验 */
    private String authMark;
    /** 排序值 */
    private Integer sort;
}
