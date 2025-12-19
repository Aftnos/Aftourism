package aftnos.aftourismserver.monitor.service;

import aftnos.aftourismserver.monitor.vo.RedisRuntimeMetricsVO;
import aftnos.aftourismserver.monitor.vo.RuntimeMetricsVO;
import aftnos.aftourismserver.monitor.vo.SystemRuntimeMetricsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 运行时指标采集服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RuntimeMetricsService {

    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 汇总 Redis 与系统运行指标
     */
    public RuntimeMetricsVO loadRuntimeMetrics() {
        RuntimeMetricsVO runtimeMetricsVO = new RuntimeMetricsVO();
        runtimeMetricsVO.setRedisMetrics(loadRedisMetrics());
        runtimeMetricsVO.setSystemMetrics(loadSystemMetrics());
        return runtimeMetricsVO;
    }

    /**
     * 拉取 Redis 实时指标
     */
    private RedisRuntimeMetricsVO loadRedisMetrics() {
        RedisRuntimeMetricsVO redisMetrics = new RedisRuntimeMetricsVO();
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            Properties stats = connection.serverCommands().info("stats");
            Properties memory = connection.serverCommands().info("memory");
            Properties commandStats = connection.serverCommands().info("commandstats");

            fillStatsMetrics(redisMetrics, stats);
            fillMemoryMetrics(redisMetrics, memory);
            fillCommandMetrics(redisMetrics, commandStats);
        } catch (Exception ex) {
            log.warn("拉取 Redis 实时指标失败", ex);
        }
        return redisMetrics;
    }

    /**
     * 将 Redis stats 信息写入指标对象
     */
    private void fillStatsMetrics(RedisRuntimeMetricsVO redisMetrics, Properties stats) {
        if (stats == null || stats.isEmpty()) {
            return;
        }
        long hits = parseLong(stats.getProperty("keyspace_hits"));
        long misses = parseLong(stats.getProperty("keyspace_misses"));
        long totalCommands = parseLong(stats.getProperty("total_commands_processed"));
        int connectedClients = (int) parseLong(stats.getProperty("connected_clients"));
        int blockedClients = (int) parseLong(stats.getProperty("blocked_clients"));
        redisMetrics.setConnectedClients(connectedClients);
        redisMetrics.setBlockedClients(blockedClients);
        redisMetrics.setKeyspaceHits(hits);
        redisMetrics.setKeyspaceMisses(misses);
        redisMetrics.setTotalCommands(totalCommands);
        long denominator = hits + misses;
        if (denominator > 0) {
            double hitRate = (double) hits / denominator * 100;
            redisMetrics.setHitRate(BigDecimal.valueOf(hitRate).setScale(2, RoundingMode.HALF_UP));
        }
    }

    /**
     * 将 Redis 内存信息写入指标对象
     */
    private void fillMemoryMetrics(RedisRuntimeMetricsVO redisMetrics, Properties memory) {
        if (memory == null || memory.isEmpty()) {
            return;
        }
        long usedMemory = parseLong(memory.getProperty("used_memory"));
        long evictedKeys = parseLong(memory.getProperty("evicted_keys"));
        double fragmentationRatio = parseDouble(memory.getProperty("mem_fragmentation_ratio"));
        redisMetrics.setUsedMemoryMb(toMb(usedMemory));
        redisMetrics.setEvictedKeys(evictedKeys);
        if (fragmentationRatio > 0) {
            redisMetrics.setMemoryFragmentationRatio(BigDecimal.valueOf(fragmentationRatio).setScale(2, RoundingMode.HALF_UP));
        }
    }

    /**
     * 将 Redis commandstats 信息写入指标对象
     */
    private void fillCommandMetrics(RedisRuntimeMetricsVO redisMetrics, Properties commandStats) {
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
        if (totalCalls > 0) {
            double avgUsec = totalUsec / totalCalls;
            redisMetrics.setAvgCommandUsec(BigDecimal.valueOf(avgUsec).setScale(2, RoundingMode.HALF_UP));
            redisMetrics.setMaxCommandUsec(BigDecimal.valueOf(maxUsecPerCall).setScale(2, RoundingMode.HALF_UP));
        }
    }

    /**
     * 拉取 JVM 与操作系统运行指标
     */
    private SystemRuntimeMetricsVO loadSystemMetrics() {
        SystemRuntimeMetricsVO systemMetrics = new SystemRuntimeMetricsVO();
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        long uptimeSeconds = runtimeMXBean.getUptime() / 1000;
        systemMetrics.setHeapUsedMb(toMb(usedMemory));
        systemMetrics.setHeapMaxMb(toMb(maxMemory));
        systemMetrics.setUptimeSeconds(uptimeSeconds);
        systemMetrics.setCpuCores(osBean.getAvailableProcessors());
        double loadAverage = osBean.getSystemLoadAverage();
        if (loadAverage >= 0) {
            systemMetrics.setSystemLoadAverage(BigDecimal.valueOf(loadAverage).setScale(2, RoundingMode.HALF_UP));
        }
        systemMetrics.setThreadCount(threadMXBean.getThreadCount());
        systemMetrics.setDaemonThreadCount(threadMXBean.getDaemonThreadCount());
        if (maxMemory > 0) {
            double heapUsage = (double) usedMemory / maxMemory * 100;
            systemMetrics.setHeapUsageRate(BigDecimal.valueOf(heapUsage).setScale(2, RoundingMode.HALF_UP));
        }

        // 扩展的操作系统指标，仅在支持的平台上可用
        if (osBean instanceof com.sun.management.OperatingSystemMXBean extendedOsBean) {
            double systemCpuLoad = extendedOsBean.getSystemCpuLoad();
            double processCpuLoad = extendedOsBean.getProcessCpuLoad();
            long totalPhysicalMemorySize = extendedOsBean.getTotalPhysicalMemorySize();
            long freePhysicalMemorySize = extendedOsBean.getFreePhysicalMemorySize();

            if (systemCpuLoad >= 0) {
                systemMetrics.setSystemCpuUsage(BigDecimal.valueOf(systemCpuLoad * 100).setScale(2, RoundingMode.HALF_UP));
            }
            if (processCpuLoad >= 0) {
                systemMetrics.setProcessCpuUsage(BigDecimal.valueOf(processCpuLoad * 100).setScale(2, RoundingMode.HALF_UP));
            }
            systemMetrics.setTotalPhysicalMemoryMb(toMb(totalPhysicalMemorySize));
            systemMetrics.setFreePhysicalMemoryMb(toMb(freePhysicalMemorySize));
        }
        return systemMetrics;
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

    /**
     * 字节转换为 MB，保留两位小数
     */
    private BigDecimal toMb(long bytes) {
        if (bytes <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        double mb = (double) bytes / 1024 / 1024;
        return BigDecimal.valueOf(mb).setScale(2, RoundingMode.HALF_UP);
    }
}
