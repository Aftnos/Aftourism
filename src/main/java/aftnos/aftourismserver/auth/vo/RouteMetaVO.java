package aftnos.aftourismserver.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * 路由元数据，完全对齐前端 RouteMeta 定义。
 */
@Data
public class RouteMetaVO {
    /** 菜单标题 */
    private String title;
    /** 菜单图标 */
    private String icon;

    /** 是否在菜单中隐藏 */
    private Boolean isHide;
    /** 是否在标签页中隐藏 */
    private Boolean isHideTab;
    /** 是否显示徽章 */
    private Boolean showBadge;
    /** 徽章文本内容 */
    private String showTextBadge;
    /** 菜单排序值 */
    private Integer sort;
    /** 是否启用 */
    private Boolean isEnable;

    /** 是否缓存 */
    private Boolean keepAlive;
    /** 是否固定标签 */
    private Boolean fixedTab;
    /** 激活菜单路径 */
    private String activePath;

    /** 外部链接地址 */
    private String link;
    /** 是否 iframe 页面 */
    private Boolean isIframe;
    /** 是否全屏 */
    private Boolean isFullPage;

    /** 允许访问的角色编码列表 */
    private List<String> roles;
    /** 操作权限列表，按 authMark 控制按钮 */
    private List<AuthPermissionVO> authList;
    /** 页面权限标识（保留给前端扩展） */
    private String authMark;

    /** 是否一级菜单标记 */
    private Boolean isFirstLevel;
    /** 父级路径，用于生成完整路径 */
    private String parentPath;
    /** 是否按钮行菜单（用于纯按钮场景） */
    private Boolean isAuthButton;
}
