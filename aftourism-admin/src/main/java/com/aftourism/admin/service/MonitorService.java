package com.aftourism.admin.service;

import com.aftourism.admin.vo.DashboardStatsVO;
import com.aftourism.admin.pojo.SystemMetric;

import java.util.List;

/**
 * 系统监控服务接口。
 */
public interface MonitorService {

    /**
     * 仪表盘统计。
     */
    DashboardStatsVO loadDashboard();

    /**
     * 最近系统指标列表。
     */
    List<SystemMetric> listLatestMetrics();
}
