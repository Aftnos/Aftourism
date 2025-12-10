package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动管理分页查询参数
 */
@Data
public class ActivityManagePageQuery {

    @Min(value = 1, message = "页码至少为1")
    private Integer current = 1;

    @Min(value = 1, message = "每页条数至少为1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer size = 10;

    /** 活动名称 */
    private String name;

    /** 活动类别 */
    private String category;

    /** 上线状态 */
    private Integer onlineStatus;

    /** 开始时间起 */
    private LocalDateTime startTimeFrom;

    /** 开始时间止 */
    private LocalDateTime startTimeTo;
}
