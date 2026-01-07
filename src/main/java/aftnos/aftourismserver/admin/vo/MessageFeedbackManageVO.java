package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言反馈管理视图对象
 */
@Data
public class MessageFeedbackManageVO {
    private Long id;
    private Long userId;
    private String userName;
    private String userNickname;
    private String title;
    private String content;
    private String contactPhone;
    private String contactEmail;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
