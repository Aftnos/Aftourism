package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言反馈展示对象
 */
@Data
public class MessageFeedbackVO {
    private Long id;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private String type;
    private String typeText;
    private String title;
    private String content;
    private Integer status;
    private String statusText;
    private Integer commentCount;
    private LocalDateTime createTime;
}
