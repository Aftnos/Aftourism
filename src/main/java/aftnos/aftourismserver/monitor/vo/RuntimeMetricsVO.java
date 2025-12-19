package aftnos.aftourismserver.monitor.vo;

import lombok.Data;

/**
 * 前端分析页所需的运行时指标聚合数据
 */
@Data
public class RuntimeMetricsVO {

    /** Redis 实时指标 */
    private RedisRuntimeMetricsVO redisMetrics;

    /** 后端 JVM 及服务器运行状态指标 */
    private SystemRuntimeMetricsVO systemMetrics;
}
