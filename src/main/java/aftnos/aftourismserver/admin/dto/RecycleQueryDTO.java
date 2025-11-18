package aftnos.aftourismserver.admin.dto;

import aftnos.aftourismserver.admin.enums.RecycleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 回收站分页查询参数
 */
@Data
public class RecycleQueryDTO {

    /** 业务类型 */
    private RecycleType type;

    /** 关键词，针对标题或名称模糊匹配 */
    private String keyword;

    /** 删除开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 删除结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 当前页，从 1 开始 */
    @Min(value = 1, message = "页码至少为 1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer pageSize = 10;
}
