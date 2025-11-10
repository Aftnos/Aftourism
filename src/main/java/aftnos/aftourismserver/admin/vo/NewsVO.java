package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻前端展示对象
 */
@Data
public class NewsVO {
    private Long id;
    private String title;
    private String content;
    private String coverUrl;
    private String author;
    private Integer status;
    private String statusText;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    private Long viewCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
