package com.aftourism.monitor.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.monitor.pojo.SiteVisitStats;
import com.aftourism.monitor.pojo.SystemMetric;
import com.aftourism.monitor.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 监控数据接口。
 */
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    @GetMapping("/metrics")
    public ResponseResult<List<SystemMetric>> metrics() {
        return ResponseResult.ok(monitorService.latestMetrics());
    }

    @GetMapping("/site-stats")
    public ResponseResult<List<SiteVisitStats>> siteStats() {
        return ResponseResult.ok(monitorService.recentSiteStats());
    }

    @GetMapping("/redis-info")
    public ResponseResult<Map<String, Object>> redisInfo() {
        return ResponseResult.ok(monitorService.redisInfo());
    }
}
