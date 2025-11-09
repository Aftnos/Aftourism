package com.aftourism.admin.service;

import com.aftourism.common.metrics.OnlineUserTracker;
import com.aftourism.common.metrics.SiteVisitCounter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

/**
 * Provides runtime statistics for the admin dashboard.
 */
@Service
@RequiredArgsConstructor
public class StatsService {

    private final SiteVisitCounter siteVisitCounter;
    private final OnlineUserTracker onlineUserTracker;
    private final ObjectProvider<RedisConnectionFactory> redisConnectionFactoryProvider;

    public Map<String, Object> currentStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVisits", siteVisitCounter.getVisitCount());
        stats.put("onlineUsers", onlineUserTracker.getCurrentOnlineUsers());
        stats.putAll(redisStats());
        stats.putAll(systemStats());
        return stats;
    }

    private Map<String, Object> redisStats() {
        Map<String, Object> redisStats = new HashMap<>();
        RedisConnectionFactory factory = redisConnectionFactoryProvider.getIfAvailable();
        if (factory != null) {
            try (RedisConnection connection = factory.getConnection()) {
                java.util.Properties info = connection.info("stats");
                String hits = info.getProperty("keyspace_hits", "0");
                String misses = info.getProperty("keyspace_misses", "0");
                long hitValue = Long.parseLong(hits);
                long missValue = Long.parseLong(misses);
                double hitRate = (hitValue + missValue) == 0 ? 1.0 : (double) hitValue / (hitValue + missValue);
                redisStats.put("cacheHits", hitValue);
                redisStats.put("cacheMisses", missValue);
                redisStats.put("cacheHitRate", hitRate);
            }
        }
        return redisStats;
    }

    private Map<String, Object> systemStats() {
        Map<String, Object> systemStats = new HashMap<>();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        systemStats.put("availableProcessors", osBean.getAvailableProcessors());
        try {
            double systemLoad = osBean.getSystemLoadAverage();
            systemStats.put("systemLoadAverage", systemLoad);
        } catch (Exception ignored) {
        }
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        systemStats.put("totalMemory", totalMemory);
        systemStats.put("freeMemory", freeMemory);
        systemStats.put("usedMemory", totalMemory - freeMemory);
        return systemStats;
    }
}
