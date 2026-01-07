package aftnos.aftourismserver.portal.enums;

/**
 * 举报原因类型枚举
 */
public enum ContentReportReasonEnum {
    /** 垃圾广告 */
    SPAM("SPAM", "垃圾广告"),
    /** 辱骂攻击 */
    ABUSE("ABUSE", "辱骂攻击"),
    /** 不当内容 */
    INAPPROPRIATE("INAPPROPRIATE", "不当内容"),
    /** 其他原因 */
    OTHER("OTHER", "其他");

    private final String code;
    private final String text;

    ContentReportReasonEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static ContentReportReasonEnum fromCode(String code) {
        if (code == null || code.isBlank()) {
            return OTHER;
        }
        for (ContentReportReasonEnum value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return OTHER;
    }
}
