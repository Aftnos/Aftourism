package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻实体类，对应表 t_news
 */
@Data
public class News {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String coverImage;
    private Integer status;
    private LocalDateTime publishTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
