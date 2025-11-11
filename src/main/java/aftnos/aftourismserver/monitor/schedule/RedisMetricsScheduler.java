package aftnos.aftourismserver.monitor.schedule;

import aftnos.aftourismserver.monitor.service.RedisMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Redis 指标采集定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMetricsScheduler {

    private final RedisMetricsService redisMetricsService;

    /** 是否开启 Redis 采样 */
    @Value("${monitor.redis.benchmark-enabled:true}")
    private boolean benchmarkEnabled;

    /**
     * 定期采集 Redis 指标
     */
    @Scheduled(fixedDelayString = "${monitor.redis.collect-interval:60000}")
    public void collect() {
        if (!benchmarkEnabled) {
            return;
        }
        log.debug("自动采集 Redis 指标任务触发");
        redisMetricsService.collectAndSaveMetrics();
    }
}
