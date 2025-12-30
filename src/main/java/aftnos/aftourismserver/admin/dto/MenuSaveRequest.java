package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单新增/修改请求参数。
 */
@Data
public class MenuSaveRequest {

    /** 父级菜单ID */
    private Long parentId;

    /** 路由名称 */
    @NotBlank(message = "路由名称不能为空")
    private String name;

    /** 路由路径 */
    @NotBlank(message = "路由路径不能为空")
    private String path;

    /** 重定向路径 */
    private String redirect;

    /** 组件路径 */
    private String component;

    /** 菜单标题 */
    @NotBlank(message = "菜单标题不能为空")
    private String title;

    /** 菜单图标 */
    private String icon;

    /** 是否隐藏菜单 */
    @NotNull(message = "隐藏菜单字段不能为空")
    @Min(value = 0, message = "隐藏菜单字段取值非法")
    @Max(value = 1, message = "隐藏菜单字段取值非法")
    private Integer isHide;

    /** 是否隐藏标签 */
    @NotNull(message = "隐藏标签字段不能为空")
    @Min(value = 0, message = "隐藏标签字段取值非法")
    @Max(value = 1, message = "隐藏标签字段取值非法")
    private Integer isHideTab;

    /** 是否显示徽章 */
    @NotNull(message = "显示徽章字段不能为空")
    @Min(value = 0, message = "显示徽章字段取值非法")
    @Max(value = 1, message = "显示徽章字段取值非法")
    private Integer showBadge;

    /** 徽章文本 */
    private String showTextBadge;

    /** 是否缓存 */
    @NotNull(message = "缓存字段不能为空")
    @Min(value = 0, message = "缓存字段取值非法")
    @Max(value = 1, message = "缓存字段取值非法")
    private Integer keepAlive;

    /** 是否固定标签 */
    @NotNull(message = "固定标签字段不能为空")
    @Min(value = 0, message = "固定标签字段取值非法")
    @Max(value = 1, message = "固定标签字段取值非法")
    private Integer fixedTab;

    /** 激活路径 */
    private String activePath;

    /** 外部链接 */
    private String link;

    /** 是否 iframe */
    @NotNull(message = "内嵌字段不能为空")
    @Min(value = 0, message = "内嵌字段取值非法")
    @Max(value = 1, message = "内嵌字段取值非法")
    private Integer isIframe;

    /** 是否全屏 */
    @NotNull(message = "全屏字段不能为空")
    @Min(value = 0, message = "全屏字段取值非法")
    @Max(value = 1, message = "全屏字段取值非法")
    private Integer isFullPage;

    /** 是否一级菜单 */
    private Integer isFirstLevel;

    /** 父级路径 */
    private String parentPath;

    /** 排序值 */
    @NotNull(message = "排序值不能为空")
    private Integer orderNum;

    /** 状态：1启用，0禁用 */
    @NotNull(message = "状态字段不能为空")
    @Min(value = 0, message = "状态字段取值非法")
    @Max(value = 1, message = "状态字段取值非法")
    private Integer status;

    /** 备注 */
    private String remark;
}
