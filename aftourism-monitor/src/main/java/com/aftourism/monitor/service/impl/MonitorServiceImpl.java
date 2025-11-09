package com.aftourism.monitor.service.impl;

import com.aftourism.monitor.mapper.SiteVisitStatsMapper;
import com.aftourism.monitor.mapper.SystemMetricMapper;
import com.aftourism.monitor.pojo.SiteVisitStats;
import com.aftourism.monitor.pojo.SystemMetric;
import com.aftourism.monitor.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 监控服务实现。
 */
@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

    private final SystemMetricMapper systemMetricMapper;
    private final SiteVisitStatsMapper siteVisitStatsMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<SystemMetric> latestMetrics() {
        return systemMetricMapper.selectLatest();
    }

    @Override
    public List<SiteVisitStats> recentSiteStats() {
        return siteVisitStatsMapper.selectRecent(7);
    }

    @Override
    public Map<String, Object> redisInfo() {
        Properties info = stringRedisTemplate.execute(RedisServerCommands::info);
        if (info == null) {
            return Collections.emptyMap();
        }
        return (Map) info;
    }
}
