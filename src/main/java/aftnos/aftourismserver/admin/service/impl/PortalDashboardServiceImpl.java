package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.mapper.PortalDashboardMapper;
import aftnos.aftourismserver.admin.service.PortalDashboardService;
import aftnos.aftourismserver.admin.vo.ContentDigestVO;
import aftnos.aftourismserver.admin.vo.ContentBriefVO;
import aftnos.aftourismserver.admin.vo.NewUserTrendVO;
import aftnos.aftourismserver.admin.vo.PortalNewUserStatsVO;
import aftnos.aftourismserver.admin.vo.PortalNewUserVO;
import aftnos.aftourismserver.admin.vo.PortalOverviewVO;
import aftnos.aftourismserver.admin.vo.VisitTrendVO;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.auth.pojo.UserRegisterStats;
import aftnos.aftourismserver.monitor.pojo.SiteVisitStats;
import aftnos.aftourismserver.monitor.service.SiteVisitStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 门户工作台统计服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PortalDashboardServiceImpl implements PortalDashboardService {

    private final SiteVisitStatsService siteVisitStatsService;
    private final UserMapper userMapper;
    private final PortalDashboardMapper portalDashboardMapper;

    @Override
    public PortalOverviewVO loadOverview() {
        LocalDate today = LocalDate.now();
        SiteVisitStats total = siteVisitStatsService.sumStats(null, null);
        SiteVisitStats todayStats = siteVisitStatsService.sumStats(today, today);
        PortalOverviewVO vo = new PortalOverviewVO();
        vo.setTotalPv(safeNumber(total.getPvCount()));
        vo.setTotalUv(safeNumber(total.getUvCount()));
        vo.setTodayPv(safeNumber(todayStats.getPvCount()));
        vo.setTodayUv(safeNumber(todayStats.getUvCount()));
        vo.setOnlineVisitors(siteVisitStatsService.countOnlineVisitors());
        vo.setContentClicks(sumContentClicks());
        vo.setNewUsersToday(countUsersByRange(today.atStartOfDay(), today.plusDays(1).atStartOfDay()));
        vo.setTotalUsers(userMapper.countAll());
        return vo;
    }

    @Override
    public List<VisitTrendVO> queryVisitTrend(int days) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days - 1L);
        List<SiteVisitStats> raw = siteVisitStatsService.listStats(start, end);
        Map<LocalDate, SiteVisitStats> mapped = raw.stream()
                .collect(Collectors.toMap(SiteVisitStats::getStatDate, it -> it));
        List<VisitTrendVO> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            SiteVisitStats item = mapped.getOrDefault(date, null);
            VisitTrendVO vo = new VisitTrendVO();
            vo.setStatDate(date);
            vo.setPvCount(item != null ? safeNumber(item.getPvCount()) : 0L);
            vo.setUvCount(item != null ? safeNumber(item.getUvCount()) : 0L);
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<VisitTrendVO> queryMonthlyTrend() {
        LocalDate today = LocalDate.now();
        YearMonth endMonth = YearMonth.from(today);
        YearMonth startMonth = endMonth.minusMonths(11);
        List<SiteVisitStats> raw = siteVisitStatsService.listStats(startMonth.atDay(1), endMonth.atEndOfMonth());
        Map<YearMonth, long[]> monthAgg = new HashMap<>();
        for (SiteVisitStats stats : raw) {
            YearMonth ym = YearMonth.from(stats.getStatDate());
            long[] agg = monthAgg.computeIfAbsent(ym, k -> new long[2]);
            agg[0] += safeNumber(stats.getPvCount());
            agg[1] += safeNumber(stats.getUvCount());
        }
        List<VisitTrendVO> list = new ArrayList<>();
        for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
            long[] agg = monthAgg.getOrDefault(month, new long[]{0L, 0L});
            VisitTrendVO vo = new VisitTrendVO();
            vo.setStatDate(month.atDay(1));
            vo.setPvCount(agg[0]);
            vo.setUvCount(agg[1]);
            list.add(vo);
        }
        return list;
    }

    @Override
    public PortalNewUserStatsVO queryNewUserStats() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        PortalNewUserStatsVO vo = new PortalNewUserStatsVO();
        vo.setNewUsersToday(countUsersByRange(today.atStartOfDay(), today.plusDays(1).atStartOfDay()));
        vo.setNewUsersThisWeek(countUsersByRange(weekStart.atStartOfDay(), today.plusDays(1).atStartOfDay()));
        vo.setLatestUsers(convertLatestUsers(userMapper.listLatest(6)));
        vo.setWeeklyTrend(convertNewUserTrend(userMapper.countDailyRegister(weekStart, today)));
        return vo;
    }

    @Override
    public ContentDigestVO queryContentDigest(int limit) {
        ContentDigestVO vo = new ContentDigestVO();
        List<ContentBriefVO> news = portalDashboardMapper.listLatestNews(limit);
        List<ContentBriefVO> notices = portalDashboardMapper.listLatestNotices(limit);
        vo.setNewsList(news != null ? news : Collections.emptyList());
        vo.setNoticeList(notices != null ? notices : Collections.emptyList());
        return vo;
    }

    /**
     * 计算内容总点击量
     */
    private long sumContentClicks() {
        long news = safeNumber(portalDashboardMapper.sumNewsViewCount());
        long notice = safeNumber(portalDashboardMapper.sumNoticeViewCount());
        long scenic = safeNumber(portalDashboardMapper.sumScenicViewCount());
        long venue = safeNumber(portalDashboardMapper.sumVenueViewCount());
        long activity = safeNumber(portalDashboardMapper.sumActivityViewCount());
        return news + notice + scenic + venue + activity;
    }

    private long safeNumber(Long value) {
        return value == null ? 0L : value;
    }

    private long countUsersByRange(LocalDateTime start, LocalDateTime end) {
        return userMapper.countByCreateTimeRange(start, end);
    }

    private List<PortalNewUserVO> convertLatestUsers(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.stream().map(user -> {
            PortalNewUserVO vo = new PortalNewUserVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setNickname(user.getNickname());
            vo.setGender(user.getGender());
            vo.setPhone(user.getPhone());
            vo.setEmail(user.getEmail());
            vo.setAvatar(user.getAvatar());
            vo.setCreateTime(user.getCreateTime());
            return vo;
        }).toList();
    }

    private List<NewUserTrendVO> convertNewUserTrend(List<UserRegisterStats> stats) {
        if (stats == null) {
            return Collections.emptyList();
        }
        return stats.stream().map(item -> {
            NewUserTrendVO vo = new NewUserTrendVO();
            vo.setStatDate(item.getStatDate());
            vo.setTotal(item.getTotal());
            return vo;
        }).collect(Collectors.toList());
    }
}
