package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.service.PortalDashboardService;
import aftnos.aftourismserver.admin.vo.ContentDigestVO;
import aftnos.aftourismserver.admin.vo.PortalNewUserStatsVO;
import aftnos.aftourismserver.admin.vo.PortalOverviewVO;
import aftnos.aftourismserver.admin.vo.VisitTrendVO;
import aftnos.aftourismserver.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工作台统计接口，提供门户访问量、在线访客、内容点击量等数据。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard/portal")
public class PortalDashboardController {

    private final PortalDashboardService portalDashboardService;

    /**
     * 工作台总览数据
     */
    @GetMapping("/overview")
    public Result<PortalOverviewVO> overview() {
        log.info("【后台-门户概览】查询总览数据");
        return Result.success(portalDashboardService.loadOverview());
    }

    /**
     * 最近 N 天访问趋势（默认7天）
     */
    @GetMapping("/visit-trend")
    public Result<List<VisitTrendVO>> visitTrend(@RequestParam(value = "days", defaultValue = "7") int days) {
        int safeDays = days <= 0 ? 7 : days;
        log.info("【后台-访问趋势】查询最近 {} 天趋势", safeDays);
        return Result.success(portalDashboardService.queryVisitTrend(safeDays));
    }

    /**
     * 近12个月访问趋势
     */
    @GetMapping("/monthly-trend")
    public Result<List<VisitTrendVO>> monthlyTrend() {
        log.info("【后台-访问趋势】查询最近12个月趋势");
        return Result.success(portalDashboardService.queryMonthlyTrend());
    }

    /**
     * 新增用户统计与最新列表
     */
    @GetMapping("/new-users")
    public Result<PortalNewUserStatsVO> newUsers() {
        log.info("【后台-新用户统计】查询新增用户概览");
        return Result.success(portalDashboardService.queryNewUserStats());
    }

    /**
     * 新闻/通知摘要列表
     */
    @GetMapping("/content-digest")
    public Result<ContentDigestVO> contentDigest(@RequestParam(value = "limit", defaultValue = "6") int limit) {
        int safeLimit = limit <= 0 ? 6 : Math.min(limit, 20);
        log.info("【后台-内容摘要】拉取最新新闻与通知，条数={}", safeLimit);
        return Result.success(portalDashboardService.queryContentDigest(safeLimit));
    }
}
