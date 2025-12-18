package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * 门户新增用户统计响应
 */
@Data
public class PortalNewUserStatsVO {
    /** 当日新增数量 */
    private Long newUsersToday;
    /** 本周新增数量 */
    private Long newUsersThisWeek;
    /** 最新注册用户列表 */
    private List<PortalNewUserVO> latestUsers;
    /** 近7天新增趋势 */
    private List<NewUserTrendVO> weeklyTrend;
}
