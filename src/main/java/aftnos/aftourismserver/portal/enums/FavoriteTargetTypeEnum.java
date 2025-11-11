package aftnos.aftourismserver.portal.enums;

/**
 * 收藏目标类型枚举，限定收藏目标范围
 */
public enum FavoriteTargetTypeEnum {

    /** 景区 */
    SCENIC,

    /** 场馆 */
    VENUE,

    /** 活动 */
    ACTIVITY;

    /**
     * 将字符串安全转换为枚举，忽略大小写
     *
     * @param code 外部传入的类型字符串
     * @return 对应的枚举实例，如不存在则返回 null
     */
    public static FavoriteTargetTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (FavoriteTargetTypeEnum value : values()) {
            if (value.name().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
