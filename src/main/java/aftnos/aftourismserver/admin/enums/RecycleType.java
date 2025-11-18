package aftnos.aftourismserver.admin.enums;

/**
 * 回收站的数据类型枚举
 */
public enum RecycleType {
    NEWS("新闻"),
    NOTICE("公告"),
    SCENIC("景区"),
    VENUE("场馆"),
    ACTIVITY("活动");

    private final String label;

    RecycleType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
