package aftnos.aftourismserver.portal.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 门户统一缓存切面，针对高频只读接口提供短 TTL 缓存，并在 Redis 不可用时自动降级。
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PortalCacheAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(portalCacheable)")
    public Object handleCache(ProceedingJoinPoint joinPoint, PortalCacheable portalCacheable) throws Throwable {
        String cacheKey = buildCacheKey(portalCacheable.cacheName(), joinPoint.getArgs());
        long ttlSeconds = portalCacheable.ttlSeconds();

        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                log.debug("【门户缓存】命中缓存，key={}，TTL={}s", cacheKey, ttlSeconds);
                return cached;
            }
        } catch (Exception ex) {
            log.warn("【门户缓存】监控告警：Redis 读取异常，key={}，将降级到数据库直查", cacheKey, ex);
            return joinPoint.proceed();
        }

        Object result = joinPoint.proceed();
        if (result == null) {
            return null;
        }

        try {
            redisTemplate.opsForValue().set(cacheKey, result, ttlSeconds, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.warn("【门户缓存】监控告警：Redis 写入异常，key={}，已降级为直查结果", cacheKey, ex);
        }
        return result;
    }

    /**
     * 拼接缓存键，包含业务名前缀与参数摘要，确保不同查询条件隔离。
     */
    private String buildCacheKey(String cacheName, Object[] args) {
        String payload = Arrays.deepToString(args);
        String digest = DigestUtils.md5DigestAsHex(payload.getBytes(StandardCharsets.UTF_8));
        return cacheName + ":" + digest;
    }
}
