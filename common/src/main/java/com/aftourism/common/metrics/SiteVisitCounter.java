package com.aftourism.common.metrics;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Tracks total visits either in Redis or via in-memory fallback.
 */
@Component
public class SiteVisitCounter {

    private static final String KEY = "site:visitCount";

    private final StringRedisTemplate stringRedisTemplate;
    private final boolean redisEnabled;
    private final AtomicLong fallbackCounter = new AtomicLong();

    public SiteVisitCounter(ObjectProvider<StringRedisTemplate> templateProvider) {
        this.stringRedisTemplate = templateProvider.getIfAvailable();
        this.redisEnabled = this.stringRedisTemplate != null;
    }

    public long increment() {
        if (redisEnabled) {
            return stringRedisTemplate.opsForValue().increment(KEY);
        }
        return fallbackCounter.incrementAndGet();
    }

    public long getVisitCount() {
        if (redisEnabled) {
            String value = stringRedisTemplate.opsForValue().get(KEY);
            return value == null ? 0L : Long.parseLong(value);
        }
        return fallbackCounter.get();
    }
}
