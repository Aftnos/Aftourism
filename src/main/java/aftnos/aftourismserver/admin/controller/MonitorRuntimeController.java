package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.monitor.service.RuntimeMetricsService;
import aftnos.aftourismserver.monitor.vo.RuntimeMetricsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统运行指标接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/monitor")
public class MonitorRuntimeController {

    private final RuntimeMetricsService runtimeMetricsService;

    /**
     * 查询 Redis 与后端运行状态
     */
    @GetMapping("/runtime")
    public Result<RuntimeMetricsVO> runtimeMetrics() {
        log.info("【后台-监控】查询运行时指标");
        return Result.success(runtimeMetricsService.loadRuntimeMetrics());
    }
}
