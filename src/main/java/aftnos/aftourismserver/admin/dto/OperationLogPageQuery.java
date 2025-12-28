package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 操作日志分页查询参数。
 */
@Data
public class OperationLogPageQuery {

    /** 操作人类型 */
    private String operatorType;

    /** 模块名称 */
    private String moduleName;

    /** 操作名称 */
    private String operationName;

    /** 是否成功 */
    private Boolean successFlag;

    /** 开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 当前页 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer current = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer size = 10;
}
