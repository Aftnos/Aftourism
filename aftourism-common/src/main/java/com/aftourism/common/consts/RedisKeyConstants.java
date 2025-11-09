package com.aftourism.common.consts;

/**
 * Redis 键统一定义，便于集中管理。
 */
public final class RedisKeyConstants {

    private RedisKeyConstants() {
    }

    /** 管理员登录会话前缀 */
    public static final String ADMIN_SESSION_PREFIX = "aftourism:admin:session:";

    /** 用户登录会话前缀 */
    public static final String USER_SESSION_PREFIX = "aftourism:portal:session:";

    /** 新闻浏览量缓存前缀 */
    public static final String NEWS_VIEW_PREFIX = "aftourism:news:view:";

    /** 活动访问统计前缀 */
    public static final String ACTIVITY_VIEW_PREFIX = "aftourism:activity:view:";
}
