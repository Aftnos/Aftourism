package aftnos.aftourismserver.portal.enums;

/**
 * 举报目标类型枚举
 */
public enum ContentReportTargetEnum {
    /** 交流文章 */
    ARTICLE("ARTICLE", "交流文章"),
    /** 交流评论 */
    COMMENT("COMMENT", "交流评论");

    private final String code;
    private final String text;

    ContentReportTargetEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static ContentReportTargetEnum fromCode(String code) {
        if (code == null || code.isBlank()) {
            return ARTICLE;
        }
        for (ContentReportTargetEnum value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return ARTICLE;
    }
}
