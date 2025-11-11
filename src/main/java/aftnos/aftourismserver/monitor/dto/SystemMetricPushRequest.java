package aftnos.aftourismserver.monitor.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 系统指标上报请求体
 */
@Data
public class SystemMetricPushRequest {

    /** 主机名或IP，不能为空 */
    @NotBlank(message = "主机名不能为空")
    private String host;

    /** CPU 使用率 */
    @NotNull(message = "CPU使用率不能为空")
    @DecimalMin(value = "0", message = "CPU使用率不能小于0")
    @DecimalMax(value = "100", message = "CPU使用率不能超过100")
    private BigDecimal cpuUsage;

    /** 内存使用率 */
    @NotNull(message = "内存使用率不能为空")
    @DecimalMin(value = "0", message = "内存使用率不能小于0")
    @DecimalMax(value = "100", message = "内存使用率不能超过100")
    private BigDecimal memoryUsage;

    /** 磁盘使用率 */
    @NotNull(message = "磁盘使用率不能为空")
    @DecimalMin(value = "0", message = "磁盘使用率不能小于0")
    @DecimalMax(value = "100", message = "磁盘使用率不能超过100")
    private BigDecimal diskUsage;

    /** 系统平均负载，可选字段 */
    private String loadAvg;

    /** 额外备注信息 */
    private String remark;
}
