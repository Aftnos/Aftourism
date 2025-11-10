package aftnos.aftourismserver.admin.enums;

import java.util.Arrays;

/**
 * 新闻状态枚举
 */
public enum NewsStatusEnum {
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    OFFLINE(2, "已下线");

    private final int code;
    private final String text;

    NewsStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static String getTextByCode(Integer code) {
        if (code == null) {
            return "未知状态";
        }
        return Arrays.stream(values())
                .filter(statusEnum -> statusEnum.code == code)
                .map(NewsStatusEnum::getText)
                .findFirst()
                .orElse("未知状态");
    }

    public static boolean isValid(Integer code) {
        if (code == null) {
            return false;
        }
        return Arrays.stream(values()).anyMatch(statusEnum -> statusEnum.code == code);
    }
}
