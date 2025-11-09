package com.aftourism.monitor.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

/**
 * 访问统计实体。
 */
@Data
public class SiteVisitStats extends BaseEntity {

    private LocalDate statDate;
    private Long pvCount;
    private Long uvCount;
    private Long ipCount;
    private String remark;
}
