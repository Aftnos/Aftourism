package com.aftourism.monitor.service;

import com.aftourism.monitor.pojo.SiteVisitStats;
import com.aftourism.monitor.pojo.SystemMetric;

import java.util.List;
import java.util.Map;

/**
 * 监控服务接口。
 */
public interface MonitorService {

    List<SystemMetric> latestMetrics();

    List<SiteVisitStats> recentSiteStats();

    Map<String, Object> redisInfo();
}
