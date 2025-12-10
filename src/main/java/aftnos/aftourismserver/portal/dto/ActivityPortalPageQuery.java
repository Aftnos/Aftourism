package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 门户活动分页查询参数
 */
@Data
public class ActivityPortalPageQuery {

    /** 页码 */
    @Min(value = 1, message = "页码不能小于1")
    private Integer current = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer size = 10;

    /** 活动名称模糊搜索 */
    private String name;

    /** 场馆ID过滤 */
    private Long venueId;

    /** 查询开始时间（活动开始时间不早于该值） */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimeFrom;

    /** 查询结束时间（活动结束时间不晚于该值） */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimeTo;
}
