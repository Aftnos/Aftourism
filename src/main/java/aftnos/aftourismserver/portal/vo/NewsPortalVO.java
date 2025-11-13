package aftnos.aftourismserver.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户新闻展示对象
 */
@Data
public class NewsPortalVO {

    /** 新闻主键 */
    private Long id;
    /** 新闻标题 */
    private String title;
    /** 内容摘要（自动截断） */
    private String summary;
    /** 封面图地址 */
    private String coverUrl;
    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    /** 发布作者 */
    private String author;
    /** 浏览量 */
    private Long viewCount;
    /** 新闻详情内容 */
    private String content;
}
