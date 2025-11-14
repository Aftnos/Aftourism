package aftnos.aftourismserver.monitor.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.monitor.dto.SystemMetricPushRequest;
import aftnos.aftourismserver.monitor.pojo.SystemMetric;
import aftnos.aftourismserver.monitor.service.SystemMetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监控指标相关接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/monitor/metrics")
public class MonitorMetricsController {

    private final SystemMetricService systemMetricService;

    /**
     * 接收外部上报的系统指标
     */
    @PostMapping("/push")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).MONITOR_SYSTEM_METRIC)")
    public Result<Void> pushMetrics(@Valid @RequestBody SystemMetricPushRequest request) {
        log.info("【系统指标上报】host={} cpu={} memory={} disk={}",
                request.getHost(), request.getCpuUsage(), request.getMemoryUsage(), request.getDiskUsage());
        SystemMetric metric = new SystemMetric();
        BeanUtils.copyProperties(request, metric);
        systemMetricService.saveMetric(metric);
        return Result.success();
    }
}
