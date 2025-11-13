package aftnos.aftourismserver.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户通知公告列表展示对象
 */
@Data
public class NoticeSummaryVO {
    /** 通知ID */
    private Long id;
    /** 通知标题 */
    private String title;
    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    /** 发布人/单位 */
    private String author;
    /** 浏览量 */
    private Long viewCount;
}
