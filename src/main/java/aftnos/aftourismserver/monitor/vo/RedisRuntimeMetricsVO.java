package aftnos.aftourismserver.monitor.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Redis 实时性能指标视图对象
 */
@Data
public class RedisRuntimeMetricsVO {

    /** 当前连接的客户端数量 */
    private Integer connectedClients;

    /** 被阻塞的客户端数量 */
    private Integer blockedClients;

    /** 命中率（百分比，0-100） */
    private BigDecimal hitRate;

    /** 累计命中次数 */
    private Long keyspaceHits;

    /** 累计未命中次数 */
    private Long keyspaceMisses;

    /** 已处理命令总数 */
    private Long totalCommands;

    /** 每条命令的平均耗时（微秒） */
    private BigDecimal avgCommandUsec;

    /** 单条命令的最大耗时（微秒） */
    private BigDecimal maxCommandUsec;

    /** 已用内存（MB） */
    private BigDecimal usedMemoryMb;

    /** 内存碎片率 */
    private BigDecimal memoryFragmentationRatio;

    /** 由于淘汰策略导致的键淘汰次数 */
    private Long evictedKeys;
}
