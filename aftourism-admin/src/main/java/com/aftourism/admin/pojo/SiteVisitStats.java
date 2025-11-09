package com.aftourism.admin.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

/**
 * 站点访问统计实体，对应 t_site_visit_stats 表。
 */
@Data
public class SiteVisitStats extends BaseEntity {

    private LocalDate statDate;
    private Long pvCount;
    private Long uvCount;
    private Long ipCount;
    private String remark;
}
