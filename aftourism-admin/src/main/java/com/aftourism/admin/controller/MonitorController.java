package com.aftourism.admin.controller;

import com.aftourism.admin.pojo.SystemMetric;
import com.aftourism.admin.service.MonitorService;
import com.aftourism.admin.vo.DashboardStatsVO;
import com.aftourism.common.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统监控接口。
 */
@RestController
@RequestMapping("/admin/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    /** 仪表盘统计 */
    @GetMapping("/dashboard")
    public ResponseResult<DashboardStatsVO> dashboard() {
        return ResponseResult.ok(monitorService.loadDashboard());
    }

    /** 系统指标列表 */
    @GetMapping("/metrics")
    public ResponseResult<List<SystemMetric>> metrics() {
        return ResponseResult.ok(monitorService.listLatestMetrics());
    }
}
