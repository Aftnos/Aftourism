package com.aftourism.monitor.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

/**
 * 系统指标实体。
 */
@Data
public class SystemMetric extends BaseEntity {

    private String host;
    private Double cpuUsage;
    private Double memoryUsage;
    private Double diskUsage;
    private String loadAvg;
    private String remark;
}
