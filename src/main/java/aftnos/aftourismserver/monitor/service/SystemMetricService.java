package aftnos.aftourismserver.monitor.service;

import aftnos.aftourismserver.monitor.mapper.SystemMetricMapper;
import aftnos.aftourismserver.monitor.pojo.SystemMetric;
import aftnos.aftourismserver.monitor.dto.SystemMetricPageQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统监控指标服务
 */
@Slf4j
@Service
public class SystemMetricService {

    private final SystemMetricMapper systemMetricMapper;

    public SystemMetricService(SystemMetricMapper systemMetricMapper) {
        this.systemMetricMapper = systemMetricMapper;
    }

    /**
     * 处理指标上报请求
     *
     * @param metric 指标实体
     */
    public void saveMetric(SystemMetric metric) {
        metric.setCreateTime(LocalDateTime.now());
        systemMetricMapper.insert(metric);
    }

    /**
     * 自动采集本机系统指标并入库
     */
    public void collectLocalMetrics() {
        try {
            SystemMetric metric = new SystemMetric();
            metric.setHost(resolveHostName());
            metric.setCpuUsage(readCpuUsage());
            metric.setMemoryUsage(readMemoryUsage());
            metric.setDiskUsage(readDiskUsage());
            metric.setLoadAvg(readLoadAverage());
            metric.setCreateTime(LocalDateTime.now());
            systemMetricMapper.insert(metric);
        } catch (Exception ex) {
            log.warn("自动采集系统指标失败", ex);
        }
    }

    /**
     * 获取主机名
     */
    private String resolveHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * 读取 CPU 使用率
     */
    private BigDecimal readCpuUsage() {
        try {
            java.lang.management.OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
                double systemCpuLoad = sunOsBean.getSystemCpuLoad();
                if (systemCpuLoad >= 0) {
                    return BigDecimal.valueOf(systemCpuLoad * 100).setScale(2, RoundingMode.HALF_UP);
                }
            }
        } catch (Exception ignore) {
            // ignore
        }
        return BigDecimal.valueOf(-1);
    }

    /**
     * 读取内存使用率
     */
    private BigDecimal readMemoryUsage() {
        try {
            java.lang.management.OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
                long totalMemory = sunOsBean.getTotalMemorySize();
                long freeMemory = sunOsBean.getFreeMemorySize();
                if (totalMemory > 0) {
                    double used = (double) (totalMemory - freeMemory) / totalMemory * 100;
                    return BigDecimal.valueOf(used).setScale(2, RoundingMode.HALF_UP);
                }
            }
        } catch (Exception ignore) {
            // ignore
        }
        return BigDecimal.valueOf(-1);
    }

    /**
     * 读取磁盘使用率（根目录）
     */
    private BigDecimal readDiskUsage() {
        try {
            File root = new File("/");
            long total = root.getTotalSpace();
            long free = root.getUsableSpace();
            if (total > 0) {
                double used = (double) (total - free) / total * 100;
                return BigDecimal.valueOf(used).setScale(2, RoundingMode.HALF_UP);
            }
        } catch (Exception ignore) {
            // ignore
        }
        return BigDecimal.valueOf(-1);
    }

    /**
     * 读取系统平均负载
     */
    private String readLoadAverage() {
        try {
            double load = java.lang.management.ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
            return load >= 0 ? String.format("%.2f", load) : null;
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 分页查询系统指标
     */
    public com.github.pagehelper.PageInfo<SystemMetric> pageMetrics(SystemMetricPageQuery query) {
        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        List<SystemMetric> list = systemMetricMapper.selectPage(query);
        return new com.github.pagehelper.PageInfo<>(list);
    }
}
