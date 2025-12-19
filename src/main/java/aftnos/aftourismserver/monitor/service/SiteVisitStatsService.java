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
import java.util.concurrent.TimeUnit;

/**
 * 站点访问统计服务
 */
@Slf4j
@Service
public class SiteVisitStatsService {

    private final SiteVisitStatsMapper siteVisitStatsMapper;

    /**
     * Redis 有序集合 key，存储在线访客标识及心跳时间戳
     */
    private static final String ONLINE_VISITOR_KEY = "portal:online:visitors";

    /**
     * UV 统计 Redis key 前缀
     */
    private static final String UV_KEY_PREFIX = "stats:uv:";

    /**
     * IP 统计 Redis key 前缀
     */
    private static final String IP_KEY_PREFIX = "stats:ip:";

    /**
     * 统计 key 过期时间
     */
    private static final Duration STATS_EXPIRE = Duration.ofDays(2);

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

            String uvRedisKey = UV_KEY_PREFIX + today;
            String ipRedisKey = IP_KEY_PREFIX + today;

            long uvAdded = addToSet(uvRedisKey, uvKey);
            long ipAdded = addToSet(ipRedisKey, safeIp);

            long uvIncrement = uvAdded > 0 ? 1L : 0L;
            long ipIncrement = ipAdded > 0 ? 1L : 0L;
            siteVisitStatsMapper.upsertVisitStats(today, 1L, uvIncrement, ipIncrement);

            // 记录在线访客心跳，基于 IP+UA 组合去重
            recordOnlineVisitor(uvKey);
        } catch (Exception ex) {
            // 拦截器中统计逻辑不能影响主流程，出现异常时仅记录日志
            log.warn("记录站点访问统计失败", ex);
        }
    }

    /**
     * 将元素写入 Redis Set，返回本次新增的数量
     */
    private long addToSet(String key, String value) {
        Long added = redisTemplate.opsForSet().add(key, value);
        // 设置过期时间，确保占用的内存会释放
        setExpireIfNecessary(key);
        return added == null ? 0L : added;
    }

    /**
     * 如果未设置过期时间，则设置为默认过期时间
     */
    private void setExpireIfNecessary(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (expire == null || expire <= 0) {
            redisTemplate.expire(key, STATS_EXPIRE);
        }
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
