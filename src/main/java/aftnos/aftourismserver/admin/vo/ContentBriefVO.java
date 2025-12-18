package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台工作台展示的内容摘要
 */
@Data
public class ContentBriefVO {

    /** 主键ID */
    private Long id;

    /** 标题 */
    private String title;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /** 浏览量/点击量 */
    private Long viewCount;
}
