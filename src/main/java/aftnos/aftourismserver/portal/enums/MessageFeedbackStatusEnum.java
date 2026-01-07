package aftnos.aftourismserver.portal.enums;

/**
 * 留言反馈处理状态
 */
public enum MessageFeedbackStatusEnum {
    PENDING(0, "待反馈"),
    RESOLVED(1, "已反馈");

    private final int code;
    private final String text;

    MessageFeedbackStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static MessageFeedbackStatusEnum fromCode(Integer code) {
        if (code == null) {
            return PENDING;
        }
        for (MessageFeedbackStatusEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return PENDING;
    }
}
