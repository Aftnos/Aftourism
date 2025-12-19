package aftnos.aftourismserver.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * 缓存管理配置，使用 Redis 存储并设置统一的序列化与 TTL。
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /** 默认缓存有效期（5 分钟） */
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    /**
     * Redis 缓存管理器配置，统一使用 JSON 序列化和自定义的 key 前缀。
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(DEFAULT_TTL)
                .computePrefixWith(cacheName -> "cache:" + cacheName + ":")
                .disableCachingNullValues();

        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(configuration)
                .build();
    }

    /**
     * 缓存错误处理器，避免因序列化或网络问题导致主流程失败。
     */
    @Bean
    public SimpleCacheErrorHandler simpleCacheErrorHandler() {
        return new SimpleCacheErrorHandler();
    }
}
