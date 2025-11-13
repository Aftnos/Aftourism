package aftnos.aftourismserver.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告新增或修改入参
 */
@Data
public class NoticeDTO {
    /** 通知标题 */
    @NotBlank(message = "通知标题不能为空")
    private String title;

    /** 通知正文内容 */
    @NotBlank(message = "通知内容不能为空")
    private String content;

    /** 发布人/单位，可为空 */
    private String author;

    /** 发布状态 */
    @NotNull(message = "通知状态不能为空")
    @Min(value = 0, message = "通知状态取值不合法")
    @Max(value = 1, message = "通知状态取值不合法")
    private Integer status;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /** 浏览量，允许为空默认 0 */
    @PositiveOrZero(message = "浏览量不能为负数")
    private Long viewCount;
}
