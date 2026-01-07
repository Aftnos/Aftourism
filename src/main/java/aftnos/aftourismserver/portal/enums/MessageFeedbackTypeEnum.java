package aftnos.aftourismserver.portal.enums;

/**
 * 留言反馈类型
 */
public enum MessageFeedbackTypeEnum {
    MESSAGE("MESSAGE", "留言"),
    FEEDBACK("FEEDBACK", "反馈");

    private final String code;
    private final String text;

    MessageFeedbackTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static MessageFeedbackTypeEnum fromCode(String code) {
        if (code == null) {
            return MESSAGE;
        }
        for (MessageFeedbackTypeEnum value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return MESSAGE;
    }
}
