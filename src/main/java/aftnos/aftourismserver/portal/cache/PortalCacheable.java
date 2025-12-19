package aftnos.aftourismserver.portal.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 门户高频只读接口缓存注解，统一设置短 TTL 缓存，避免重复查询数据库。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PortalCacheable {

    /**
     * 缓存名称前缀，用于快速定位业务模块。
     */
    String cacheName();

    /**
     * 缓存过期时间，单位：秒，默认 60 秒短周期，确保页面实时性。
     */
    long ttlSeconds() default 60L;
}
