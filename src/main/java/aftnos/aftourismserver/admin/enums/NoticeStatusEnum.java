package aftnos.aftourismserver.admin.enums;

import java.util.Arrays;

/**
 * 通知公告状态枚举
 */
public enum NoticeStatusEnum {
    OFFLINE(0, "已下线"),
    PUBLISHED(1, "已发布");

    private final int code;
    private final String text;

    NoticeStatusEnum(int code, String text) {
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
                .map(NoticeStatusEnum::getText)
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
