package aftnos.aftourismserver.admin.vo;

import lombok.Data;

/**
 * 门户工作台总览数据
 */
@Data
public class PortalOverviewVO {

    /** 累计访问次数（PV 总量） */
    private Long totalPv;

    /** 累计独立访客（UV 总量） */
    private Long totalUv;

    /** 今日访问次数 */
    private Long todayPv;

    /** 今日独立访客 */
    private Long todayUv;

    /** 实时在线访客数量 */
    private Long onlineVisitors;

    /** 门户内容总点击量（新闻/公告/景区/场馆/活动） */
    private Long contentClicks;

    /** 当日新增门户用户 */
    private Long newUsersToday;

    /** 门户用户总数 */
    private Long totalUsers;
}
