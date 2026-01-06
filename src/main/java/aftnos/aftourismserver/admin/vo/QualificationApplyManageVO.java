package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理端资质申请列表视图对象。
 */
@Data
public class QualificationApplyManageVO {

    /** 申请ID */
    private Long id;

    /** 申请用户ID */
    private Long userId;

    /** 申请用户账号 */
    private String userName;

    /** 申请人姓名 */
    private String realName;

    /** 单位/机构名称 */
    private String organization;

    /** 联系电话 */
    private String contactPhone;

    /** 资质申请说明 */
    private String applyReason;

    /** 资质附件链接 */
    private String attachmentUrl;

    /** 审核状态 */
    private Integer applyStatus;

    /** 审核备注 */
    private String auditRemark;

    /** 提交时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
