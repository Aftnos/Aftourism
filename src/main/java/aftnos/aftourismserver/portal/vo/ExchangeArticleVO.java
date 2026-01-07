package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交流文章展示对象
 */
@Data
public class ExchangeArticleVO {
    private Long id;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private String title;
    private String content;
    private String coverUrl;
    private Integer status;
    private String statusText;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;
}
