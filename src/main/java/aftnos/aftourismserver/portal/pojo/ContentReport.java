package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 举报记录实体，映射 t_content_report 表
 */
@Data
public class ContentReport {
    private Long id;
    /** 举报人用户ID */
    private Long reporterId;
    /** 被举报人用户ID */
    private Long targetUserId;
    /** 举报目标类型 */
    private String targetType;
    /** 举报目标ID */
    private Long targetId;
    /** 举报原因类型 */
    private String reasonType;
    /** 举报原因描述 */
    private String reason;
    /** 截图URL集合(JSON字符串) */
    private String screenshotUrls;
    /** 处理状态 */
    private Integer status;
    /** 处理结果说明 */
    private String resultRemark;
    /** 是否违规 */
    private Integer violationFlag;
    /** 逻辑删除标记 */
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
