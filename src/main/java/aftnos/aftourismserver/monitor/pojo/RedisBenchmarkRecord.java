package aftnos.aftourismserver.monitor.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Redis 性能采样记录实体
 * 对应表：t_redis_benchmark
 */
@Data
public class RedisBenchmarkRecord {

    /** 主键ID */
    private Long id;

    /** 采集时间 */
    private LocalDateTime metricTime;

    /** 连接池活跃连接数 */
    private Integer poolActive;

    /** 连接池空闲连接数 */
    private Integer poolIdle;

    /** 连接池等待线程数 */
    private Integer poolWaiting;

    /** 命中率（百分比） */
    private BigDecimal hitRate;

    /** keyspace hits 累计次数 */
    private Long keyspaceHits;

    /** keyspace misses 累计次数 */
    private Long keyspaceMisses;

    /** 总命令执行次数 */
    private Long totalCommands;

    /** 平均每条命令耗时（微秒） */
    private BigDecimal avgCommandUsec;

    /** 单条命令最大耗时（微秒） */
    private BigDecimal maxCommandUsec;

    /** 备注信息 */
    private String remark;

    /** 逻辑删除标记 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
