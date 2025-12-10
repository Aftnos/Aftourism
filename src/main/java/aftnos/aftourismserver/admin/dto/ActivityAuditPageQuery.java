package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

/**
 * 活动审核分页查询参数
 */
@Data
public class ActivityAuditPageQuery {
    /** 当前页，从 1 开始 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer current = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer size = 10;

    /** 活动名称模糊匹配 */
    private String name;

    /** 审核状态筛选：0待审核 1已通过 2已拒绝 */
    private Integer applyStatus;

    /** 上线状态筛选：0下线 1上线 */
    private Integer onlineStatus;

    /** 查询开始时间（活动开始时间不早于该值） */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime startTimeFrom;

    /** 查询结束时间（活动开始时间不晚于该值） */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime startTimeTo;
}