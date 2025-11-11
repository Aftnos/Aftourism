package aftnos.aftourismserver.monitor.schedule;

import aftnos.aftourismserver.monitor.service.SystemMetricService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 系统指标自动采集任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemMetricScheduler {

    private final SystemMetricService systemMetricService;

    /** 是否开启自动采集 */
    @Value("${monitor.metrics.auto-collect-enabled:false}")
    private boolean autoCollectEnabled;

    /**
     * 固定间隔采集系统指标
     */
    @Scheduled(fixedDelayString = "${monitor.metrics.collect-interval:60000}")
    public void collect() {
        if (!autoCollectEnabled) {
            return;
        }
        log.debug("自动采集系统指标任务触发");
        systemMetricService.collectLocalMetrics();
    }
}
