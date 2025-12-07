package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单实体，对应 t_menu，用于对接前端动态路由。
 */
@Data
public class Menu {

    /** 主键ID */
    private Long id;
    /** 父级菜单ID，0表示顶级 */
    private Long parentId;
    /** 路由名称 */
    private String name;
    /** 路由路径 */
    private String path;
    /** 重定向路径 */
    private String redirect;
    /** 组件路径 */
    private String component;

    /** 元信息：标题 */
    private String title;
    /** 元信息：图标 */
    private String icon;
    /** 元信息：是否隐藏菜单 */
    private Integer isHide;
    /** 元信息：是否隐藏标签 */
    private Integer isHideTab;
    /** 元信息：是否显示徽章 */
    private Integer showBadge;
    /** 元信息：徽章文本 */
    private String showTextBadge;
    /** 元信息：是否缓存页面 */
    private Integer keepAlive;
    /** 元信息：是否固定标签 */
    private Integer fixedTab;
    /** 元信息：激活路径 */
    private String activePath;
    /** 元信息：外链地址 */
    private String link;
    /** 元信息：是否为 iframe */
    private Integer isIframe;
    /** 元信息：是否全屏 */
    private Integer isFullPage;
    /** 元信息：是否一级菜单标记 */
    private Integer isFirstLevel;
    /** 元信息：父级路径 */
    private String parentPath;

    /** 排序 */
    private Integer orderNum;
    /** 状态 */
    private Integer status;
    /** 备注 */
    private String remark;
    /** 逻辑删除标记 */
    private Integer isDeleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
