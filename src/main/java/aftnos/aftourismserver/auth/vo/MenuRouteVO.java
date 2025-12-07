package aftnos.aftourismserver.auth.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单路由节点，结构与前端 AppRouteRecord 对齐。
 */
@Data
public class MenuRouteVO {
    /** 菜单 ID，方便前端缓存（可选）。 */
    private Long id;
    /** 路由路径 */
    private String path;
    /** 路由名称 */
    private String name;
    /** 重定向地址 */
    private String redirect;
    /** 组件路径 */
    private String component;
    /** 元数据 */
    private RouteMetaVO meta;

    /** 子菜单 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuRouteVO> children = new ArrayList<>();
}
