package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交流文章管理视图对象
 */
@Data
public class ExchangeArticleManageVO {
    private Long id;
    private Long userId;
    private String userName;
    private String userNickname;
    private String title;
    private String coverUrl;
    private Integer status;
    private String statusText;
    private Integer likeCount;
    private Integer commentCount;
    private String auditRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
