package aftnos.aftourismserver.admin.enums;

/**
 * 活动上下线状态
 */
public enum ActivityOnlineStatusEnum {
    OFFLINE(0, "已下线"),
    ONLINE(1, "已上线");

    private final int code;
    private final String text;

    ActivityOnlineStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
