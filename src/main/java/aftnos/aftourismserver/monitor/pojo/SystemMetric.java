package aftnos.aftourismserver.monitor.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 系统监控指标实体
 * 对应表：t_system_metric
 */
@Data
public class SystemMetric {

    /** 主键ID */
    private Long id;

    /** 主机名或IP */
    private String host;

    /** CPU 使用率（百分比） */
    private BigDecimal cpuUsage;

    /** 内存使用率（百分比） */
    private BigDecimal memoryUsage;

    /** 磁盘使用率（百分比） */
    private BigDecimal diskUsage;

    /** 系统负载信息 */
    private String loadAvg;

    /** 备注信息 */
    private String remark;

    /** 逻辑删除标记 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
