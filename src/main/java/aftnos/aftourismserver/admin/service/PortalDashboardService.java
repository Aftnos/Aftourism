package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.vo.ContentDigestVO;
import aftnos.aftourismserver.admin.vo.PortalNewUserStatsVO;
import aftnos.aftourismserver.admin.vo.PortalOverviewVO;
import aftnos.aftourismserver.admin.vo.VisitTrendVO;
import java.util.List;

/**
 * 门户工作台统计服务
 */
public interface PortalDashboardService {

    /**
     * 查询门户访问与在线概览
     */
    PortalOverviewVO loadOverview();

    /**
     * 获取最近若干天的访问趋势
     */
    List<VisitTrendVO> queryVisitTrend(int days);

    /**
     * 获取按月聚合的访问趋势（近12个月）
     */
    List<VisitTrendVO> queryMonthlyTrend();

    /**
     * 新增用户统计与列表
     */
    PortalNewUserStatsVO queryNewUserStats();

    /**
     * 新闻/公告摘要
     */
    ContentDigestVO queryContentDigest(int limit);
}
