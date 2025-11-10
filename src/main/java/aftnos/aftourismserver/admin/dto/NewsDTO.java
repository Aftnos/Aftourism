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
 * 新增或修改新闻的入参
 */
@Data
public class NewsDTO {
    @NotBlank(message = "新闻标题不能为空")
    private String title;

    @NotBlank(message = "新闻内容不能为空")
    private String content;

    @NotBlank(message = "请上传封面图")
    private String coverUrl;

    private String author;

    @NotNull(message = "新闻状态不能为空")
    @Min(value = 0, message = "新闻状态值不合法")
    @Max(value = 1, message = "新闻状态值不合法")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @PositiveOrZero(message = "浏览量不能为负数")
    private Long viewCount;
}
