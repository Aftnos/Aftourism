package aftnos.aftourismserver.admin.enums;

/**
 * 活动申报状态枚举
 */
public enum ActivityApplyStatusEnum {
    PENDING(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核不通过");

    private final int code;
    private final String text;

    ActivityApplyStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    /**
     * 根据状态码获取枚举
     */
    public static ActivityApplyStatusEnum fromCode(Integer code) {
        if (code == null) {
            return PENDING;
        }
        for (ActivityApplyStatusEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return PENDING;
    }
}
