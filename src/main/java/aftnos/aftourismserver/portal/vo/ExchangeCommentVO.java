package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交流评论视图对象
 */
@Data
public class ExchangeCommentVO {
    private Long id;
    private Long articleId;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private String content;
    private Long parentId;
    private Long mentionUserId;
    private String mentionUserNickname;
    private String mentionUserAvatar;
    private Integer likeCount;
    private LocalDateTime createTime;
    private List<ExchangeCommentVO> children;
}
