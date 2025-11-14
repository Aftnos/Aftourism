package aftnos.aftourismserver.common.security;

/**
 * 后台功能所需的资源-动作权限点定义。
 */
public enum AdminPermission {

    // 新闻管理
    NEWS_CREATE("NEWS", "CREATE", "新闻-新增"),
    NEWS_UPDATE("NEWS", "UPDATE", "新闻-修改"),
    NEWS_DELETE("NEWS", "DELETE", "新闻-删除"),
    NEWS_READ("NEWS", "READ", "新闻-查看"),

    // 通知公告
    NOTICE_CREATE("NOTICE", "CREATE", "通知-新增"),
    NOTICE_UPDATE("NOTICE", "UPDATE", "通知-修改"),
    NOTICE_DELETE("NOTICE", "DELETE", "通知-删除"),
    NOTICE_READ("NOTICE", "READ", "通知-查看"),

    // 景区管理
    SCENIC_CREATE("SCENIC", "CREATE", "景区-新增"),
    SCENIC_UPDATE("SCENIC", "UPDATE", "景区-修改"),
    SCENIC_DELETE("SCENIC", "DELETE", "景区-删除"),
    SCENIC_READ("SCENIC", "READ", "景区-查看"),

    // 场馆管理
    VENUE_CREATE("VENUE", "CREATE", "场馆-新增"),
    VENUE_UPDATE("VENUE", "UPDATE", "场馆-修改"),
    VENUE_DELETE("VENUE", "DELETE", "场馆-删除"),
    VENUE_READ("VENUE", "READ", "场馆-查看"),

    // 活动审核
    ACTIVITY_APPROVE("ACTIVITY_REVIEW", "APPROVE", "活动审核-通过"),
    ACTIVITY_REJECT("ACTIVITY_REVIEW", "REJECT", "活动审核-驳回"),
    ACTIVITY_ONLINE("ACTIVITY_REVIEW", "ONLINE", "活动审核-上线"),
    ACTIVITY_OFFLINE("ACTIVITY_REVIEW", "OFFLINE", "活动审核-下线"),

    // 文件上传
    FILE_UPLOAD("FILE", "UPLOAD", "文件-上传"),

    // 回收站
    RECYCLE_READ("RECYCLE_BIN", "READ", "回收站-查看"),
    RECYCLE_RESTORE("RECYCLE_BIN", "RESTORE", "回收站-恢复"),
    RECYCLE_DELETE("RECYCLE_BIN", "DELETE", "回收站-彻底删除"),

    // 系统监控
    MONITOR_SYSTEM_METRIC("MONITOR", "SYSTEM_METRIC", "监控-系统指标上报"),

    // 管理员账户管理
    ADMIN_ACCOUNT_CREATE("ADMIN_ACCOUNT", "CREATE", "管理员-新增"),
    ADMIN_ACCOUNT_UPDATE("ADMIN_ACCOUNT", "UPDATE", "管理员-修改/分配角色"),
    ADMIN_ACCOUNT_DELETE("ADMIN_ACCOUNT", "DELETE", "管理员-删除"),
    ADMIN_ACCOUNT_READ("ADMIN_ACCOUNT", "READ", "管理员-查看"),

    // 门户用户管理
    PORTAL_USER_READ("PORTAL_USER", "READ", "门户用户-查看"),
    PORTAL_USER_UPDATE("PORTAL_USER", "UPDATE", "门户用户-编辑角色/状态"),

    // 角色权限管理
    ROLE_ACCESS_READ("ROLE_ACCESS", "READ", "角色权限-查看"),
    ROLE_ACCESS_UPDATE("ROLE_ACCESS", "UPDATE", "角色权限-配置");

    private final String resourceKey;
    private final String action;
    private final String description;

    AdminPermission(String resourceKey, String action, String description) {
        this.resourceKey = resourceKey;
        this.action = action;
        this.description = description;
    }

    /**
     * 资源键。
     */
    public String resourceKey() {
        return resourceKey;
    }

    /**
     * 动作键。
     */
    public String action() {
        return action;
    }

    /**
     * 拼接后的权限键，便于展示。
     */
    public String asKey() {
        return resourceKey + ":" + action;
    }

    /**
     * 权限点描述信息。
     */
    public String description() {
        return description;
    }
}
