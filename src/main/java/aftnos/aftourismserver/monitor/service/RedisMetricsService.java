package aftnos.aftourismserver.monitor.service;

import aftnos.aftourismserver.monitor.mapper.RedisBenchmarkMapper;
import aftnos.aftourismserver.monitor.pojo.RedisBenchmarkRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Redis 指标采集服务
 */
@Slf4j
@Service
public class RedisMetricsService {

    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisBenchmarkMapper redisBenchmarkMapper;

    public RedisMetricsService(RedisConnectionFactory redisConnectionFactory,
                               RedisBenchmarkMapper redisBenchmarkMapper) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.redisBenchmarkMapper = redisBenchmarkMapper;
    }

    /**
     * 采集一次 Redis 指标并入库
     */
    public void collectAndSaveMetrics() {
        try {
            RedisBenchmarkRecord record = new RedisBenchmarkRecord();
            record.setMetricTime(LocalDateTime.now());

            try (RedisConnection connection = redisConnectionFactory.getConnection()) {
                fillCommandMetrics(record, connection.serverCommands().info("commandstats"));
                fillStatsMetrics(record, connection.serverCommands().info("stats"));
            }

            redisBenchmarkMapper.insert(record);
        } catch (Exception ex) {
            log.warn("采集 Redis 指标失败", ex);
        }
    }

    /**
     * 填充命令执行相关指标
     */
    private void fillCommandMetrics(RedisBenchmarkRecord record, Properties commandStats) {
        if (commandStats == null || commandStats.isEmpty()) {
            return;
        }
        long totalCalls = 0;
        double totalUsec = 0;
        double maxUsecPerCall = 0;
        for (String key : commandStats.stringPropertyNames()) {
            String value = commandStats.getProperty(key);
            Map<String, String> metrics = Arrays.stream(value.split(","))
                    .map(pair -> pair.split("=", 2))
                    .filter(arr -> arr.length == 2)
                    .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
            long calls = parseLong(metrics.get("calls"));
            double usec = parseDouble(metrics.get("usec"));
            double usecPerCall = parseDouble(metrics.get("usec_per_call"));
            totalCalls += calls;
            totalUsec += usec;
            if (usecPerCall > maxUsecPerCall) {
                maxUsecPerCall = usecPerCall;
            }
        }
        record.setTotalCommands(totalCalls);
        if (totalCalls > 0) {
            double avgUsec = totalUsec / totalCalls;
            record.setAvgCommandUsec(BigDecimal.valueOf(avgUsec).setScale(2, RoundingMode.HALF_UP));
            record.setMaxCommandUsec(BigDecimal.valueOf(maxUsecPerCall).setScale(2, RoundingMode.HALF_UP));
        }
    }

    /**
     * 填充 Redis stats 指标（命中率等）
     */
    private void fillStatsMetrics(RedisBenchmarkRecord record, Properties stats) {
        if (stats == null || stats.isEmpty()) {
            return;
        }
        long hits = parseLong(stats.getProperty("keyspace_hits"));
        long misses = parseLong(stats.getProperty("keyspace_misses"));
        long totalCommands = parseLong(stats.getProperty("total_commands_processed"));
        int connectedClients = (int) parseLong(stats.getProperty("connected_clients"));
        int blockedClients = (int) parseLong(stats.getProperty("blocked_clients"));
        int trackingClients = (int) parseLong(stats.getProperty("tracking_clients"));
        record.setPoolActive(connectedClients);
        record.setPoolIdle(trackingClients);
        record.setPoolWaiting(blockedClients);
        record.setKeyspaceHits(hits);
        record.setKeyspaceMisses(misses);
        record.setTotalCommands(totalCommands);
        long denominator = hits + misses;
        if (denominator > 0) {
            double hitRate = (double) hits / denominator * 100;
            record.setHitRate(BigDecimal.valueOf(hitRate).setScale(2, RoundingMode.HALF_UP));
        }
    }

    private long parseLong(String value) {
        try {
            return value == null ? 0 : Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return value == null ? 0 : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
