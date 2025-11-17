package aftnos.aftourismserver.monitor.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 系统指标分页查询参数
 */
@Data
public class SystemMetricPageQuery {

    /** 主机名或IP，精确匹配 */
    private String host;

    /** 指标类型：CPU/MEMORY/DISK */
    private String metricType;

    /** 指标最小值（百分比） */
    private BigDecimal minValue;

    /** 指标最大值（百分比） */
    private BigDecimal maxValue;

    /** 开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 页码 */
    @Min(1)
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(1)
    @Max(100)
    private Integer pageSize = 10;
}