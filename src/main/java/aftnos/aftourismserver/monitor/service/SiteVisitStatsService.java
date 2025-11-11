package aftnos.aftourismserver.monitor.service;

import aftnos.aftourismserver.monitor.mapper.SiteVisitStatsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 站点访问统计服务
 */
@Slf4j
@Service
public class SiteVisitStatsService {

    private final SiteVisitStatsMapper siteVisitStatsMapper;

    /**
     * UV 缓存：key 为日期，value 为当日已经统计过的 IP+UA 组合
     */
    private final Map<LocalDate, Set<String>> uvCache = new ConcurrentHashMap<>();

    /**
     * IP 缓存：key 为日期，value 为当日已经出现过的独立 IP
     */
    private final Map<LocalDate, Set<String>> ipCache = new ConcurrentHashMap<>();

    public SiteVisitStatsService(SiteVisitStatsMapper siteVisitStatsMapper) {
        this.siteVisitStatsMapper = siteVisitStatsMapper;
    }

    /**
     * 记录一次站点访问
     *
     * @param ip        访问者 IP
     * @param userAgent User-Agent 信息
     */
    public void recordVisit(String ip, String userAgent) {
        try {
            LocalDate today = LocalDate.now();
            String safeIp = StringUtils.hasText(ip) ? ip : "UNKNOWN";
            String ua = StringUtils.hasText(userAgent) ? userAgent : "UNKNOWN";
            String uvKey = safeIp + '|' + ua;

            boolean newUv = uvCache
                    .computeIfAbsent(today, key -> ConcurrentHashMap.newKeySet())
                    .add(uvKey);
            boolean newIp = ipCache
                    .computeIfAbsent(today, key -> ConcurrentHashMap.newKeySet())
                    .add(safeIp);

            // 清理过期的缓存数据，确保内存不会无限增长
            cleanupCache(today);

            long uvIncrement = newUv ? 1L : 0L;
            long ipIncrement = newIp ? 1L : 0L;
            siteVisitStatsMapper.upsertVisitStats(today, 1L, uvIncrement, ipIncrement);
        } catch (Exception ex) {
            // 拦截器中统计逻辑不能影响主流程，出现异常时仅记录日志
            log.warn("记录站点访问统计失败", ex);
        }
    }

    /**
     * 仅保留当日的缓存集合，避免日期变化后集合持续累积
     */
    private void cleanupCache(LocalDate today) {
        uvCache.keySet().removeIf(date -> date.isBefore(today));
        ipCache.keySet().removeIf(date -> date.isBefore(today));
    }
}
