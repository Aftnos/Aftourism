package aftnos.aftourismserver.portal.enums;

/**
 * 交流文章状态枚举
 */
public enum ExchangeArticleStatusEnum {
    /** 待审核 */
    PENDING(0, "待审核"),
    /** 已发布 */
    APPROVED(1, "已发布"),
    /** 已驳回 */
    REJECTED(2, "已驳回");

    private final int code;
    private final String text;

    ExchangeArticleStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static ExchangeArticleStatusEnum fromCode(Integer code) {
        if (code == null) {
            return PENDING;
        }
        for (ExchangeArticleStatusEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return PENDING;
    }
}
