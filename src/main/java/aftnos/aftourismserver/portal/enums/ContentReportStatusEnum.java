package aftnos.aftourismserver.portal.enums;

/**
 * 举报处理状态枚举
 */
public enum ContentReportStatusEnum {
    /** 待处理 */
    PENDING(0, "待处理"),
    /** 已处理 */
    PROCESSED(1, "已处理"),
    /** 已驳回 */
    REJECTED(2, "已驳回");

    private final int code;
    private final String text;

    ContentReportStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static ContentReportStatusEnum fromCode(Integer code) {
        if (code == null) {
            return PENDING;
        }
        for (ContentReportStatusEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return PENDING;
    }
}
