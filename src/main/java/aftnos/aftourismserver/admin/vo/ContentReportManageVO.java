package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 举报管理视图对象
 */
@Data
public class ContentReportManageVO {
    private Long id;
    private Long reporterId;
    private String reporterName;
    private String reporterNickname;
    private Long targetUserId;
    private String targetUserName;
    private String targetUserNickname;
    private String targetType;
    private String targetTypeText;
    private Long targetId;
    private String reasonType;
    private String reasonTypeText;
    private String reason;
    private String screenshotUrls;
    private Integer status;
    private String statusText;
    private Integer violationFlag;
    private String resultRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
