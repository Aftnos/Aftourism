package com.aftourism.admin.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

/**
 * 系统资源监控实体，对应 t_system_metric 表。
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
