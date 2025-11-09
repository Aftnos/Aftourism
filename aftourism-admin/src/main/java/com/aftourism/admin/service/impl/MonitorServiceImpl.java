package com.aftourism.admin.service.impl;

import com.aftourism.admin.mapper.SiteVisitStatsMapper;
import com.aftourism.admin.mapper.SystemMetricMapper;
import com.aftourism.admin.pojo.SiteVisitStats;
import com.aftourism.admin.pojo.SystemMetric;
import com.aftourism.admin.service.MonitorService;
import com.aftourism.admin.vo.DashboardStatsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统监控服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

    private final SiteVisitStatsMapper siteVisitStatsMapper;
    private final SystemMetricMapper systemMetricMapper;

    @Override
    public DashboardStatsVO loadDashboard() {
        List<SiteVisitStats> recent = siteVisitStatsMapper.selectRecent(7);
        List<Long> visitList = recent.stream()
                .map(SiteVisitStats::getPvCount)
                .collect(Collectors.toList());
        DashboardStatsVO vo = DashboardStatsVO.builder()
                .userCount(0L)
                .pendingActivityCount(0L)
                .recentVisitCount(visitList)
                .build();
        log.info("加载仪表盘统计，共{}天数据", visitList.size());
        return vo;
    }

    @Override
    public List<SystemMetric> listLatestMetrics() {
        return systemMetricMapper.selectLatest();
    }
}
