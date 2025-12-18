package aftnos.aftourismserver.monitor.service;

import aftnos.aftourismserver.monitor.mapper.SiteVisitStatsMapper;
import aftnos.aftourismserver.monitor.pojo.SiteVisitStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
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

    /**
     * Redis 有序集合 key，存储在线访客标识及心跳时间戳
     */
    private static final String ONLINE_VISITOR_KEY = "portal:online:visitors";

    /**
     * 在线访客有效期，超过窗口未上报则视为离线
     */
    private static final Duration ONLINE_EXPIRE = Duration.ofMinutes(5);

    private final RedisTemplate<String, Object> redisTemplate;

    public SiteVisitStatsService(SiteVisitStatsMapper siteVisitStatsMapper, RedisTemplate<String, Object> redisTemplate) {
        this.siteVisitStatsMapper = siteVisitStatsMapper;
        this.redisTemplate = redisTemplate;
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

            // 记录在线访客心跳，基于 IP+UA 组合去重
            recordOnlineVisitor(uvKey);
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

    /**
     * 记录在线访客信息，使用有序集合存储心跳时间，定期裁剪过期数据
     *
     * @param visitorKey IP+UA 组合
     */
    private void recordOnlineVisitor(String visitorKey) {
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        ops.add(ONLINE_VISITOR_KEY, visitorKey, now);
        long expireBefore = now - ONLINE_EXPIRE.getSeconds();
        ops.removeRangeByScore(ONLINE_VISITOR_KEY, 0, expireBefore);
        // 额外设置 key 过期时间，防止长期无访问时残留
        redisTemplate.expire(ONLINE_VISITOR_KEY, ONLINE_EXPIRE.plusMinutes(1));
    }

    /**
     * 计算当前在线访客数量（最近 ONLINE_EXPIRE 窗口）
     */
    public long countOnlineVisitors() {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        Long size = ops.zCard(ONLINE_VISITOR_KEY);
        return size == null ? 0L : size;
    }

    /**
     * 查询指定时间范围内的访问统计列表。
     *
     * @param start 开始日期（包含）
     * @param end   结束日期（包含）
     * @return 日志列表，按日期升序
     */
    public List<SiteVisitStats> listStats(LocalDate start, LocalDate end) {
        List<SiteVisitStats> stats = siteVisitStatsMapper.selectByDateRange(start, end);
        return stats == null ? Collections.emptyList() : stats;
    }

    /**
     * 汇总指定时间范围内的 PV/UV/IP
     *
     * @param start 开始日期（可空）
     * @param end   结束日期（可空）
     * @return 聚合后的统计对象，未查询到数据时返回 0
     */
    public SiteVisitStats sumStats(LocalDate start, LocalDate end) {
        SiteVisitStats sum = siteVisitStatsMapper.sumByDateRange(start, end);
        if (sum == null) {
            sum = new SiteVisitStats();
            sum.setPvCount(0L);
            sum.setUvCount(0L);
            sum.setIpCount(0L);
        }
        return sum;
    }
}
