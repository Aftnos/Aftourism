package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言反馈实体，映射 t_message_feedback 表
 */
@Data
public class MessageFeedback {
    private Long id;
    /** 提交用户ID */
    private Long userId;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 联系电话 */
    private String contactPhone;
    /** 联系邮箱 */
    private String contactEmail;
    /** 反馈状态：0待反馈 1已反馈 */
    private Integer status;
    /** 逻辑删除标记 */
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
