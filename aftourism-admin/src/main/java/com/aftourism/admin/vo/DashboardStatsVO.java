package com.aftourism.admin.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 后台仪表盘统计数据。
 */
@Data
@Builder
public class DashboardStatsVO {

    /** 总用户数 */
    private Long userCount;

    /** 活动待审数量 */
    private Long pendingActivityCount;

    /** 近七日访问数据 */
    private List<Long> recentVisitCount;
}
