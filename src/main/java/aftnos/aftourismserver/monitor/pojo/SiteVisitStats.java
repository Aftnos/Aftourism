package aftnos.aftourismserver.monitor.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 站点访问统计实体
 * 对应表：t_site_visit_stats
 */
@Data
public class SiteVisitStats {

    /** 主键ID */
    private Long id;

    /** 统计日期 */
    private LocalDate statDate;

    /** 页面访问次数（PV） */
    private Long pvCount;

    /** 独立访客数（UV） */
    private Long uvCount;

    /** 独立IP数量 */
    private Long ipCount;

    /** 备注信息 */
    private String remark;

    /** 逻辑删除标记 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
