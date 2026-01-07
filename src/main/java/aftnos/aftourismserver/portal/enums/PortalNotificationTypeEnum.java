package aftnos.aftourismserver.portal.enums;

/**
 * 门户通知类型枚举
 */
public enum PortalNotificationTypeEnum {
    /** 点赞提醒 */
    LIKE("LIKE", "点赞提醒"),
    /** 回复提醒 */
    REPLY("REPLY", "回复提醒"),
    /** 违规提醒 */
    VIOLATION("VIOLATION", "违规提醒"),
    /** 审核提醒 */
    AUDIT("AUDIT", "审核提醒"),
    /** 举报处理提醒 */
    REPORT("REPORT", "举报处理提醒");

    private final String code;
    private final String text;

    PortalNotificationTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static PortalNotificationTypeEnum fromCode(String code) {
        if (code == null || code.isBlank()) {
            return REPLY;
        }
        for (PortalNotificationTypeEnum value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return REPLY;
    }
}
