package aftnos.aftourismserver.monitor.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后端系统运行时指标视图对象
 */
@Data
public class SystemRuntimeMetricsVO {

    /** 系统运行的总时长（秒） */
    private Long uptimeSeconds;

    /** CPU 核心数 */
    private Integer cpuCores;

    /** 系统平均负载 */
    private BigDecimal systemLoadAverage;

    /** 系统整体 CPU 使用率（百分比） */
    private BigDecimal systemCpuUsage;

    /** 当前进程 CPU 使用率（百分比） */
    private BigDecimal processCpuUsage;

    /** 堆内存已使用（MB） */
    private BigDecimal heapUsedMb;

    /** 堆内存最大可用（MB） */
    private BigDecimal heapMaxMb;

    /** 堆内存使用率（百分比） */
    private BigDecimal heapUsageRate;

    /** 活跃线程数 */
    private Integer threadCount;

    /** 守护线程数 */
    private Integer daemonThreadCount;

    /** 可用物理内存（MB） */
    private BigDecimal freePhysicalMemoryMb;

    /** 物理内存总量（MB） */
    private BigDecimal totalPhysicalMemoryMb;
}
