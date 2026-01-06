package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 对应数据库 t_user_qualification_apply 表的实体对象。
 */
@Data
public class UserQualificationApply {

    /** 主键ID */
    private Long id;

    /** 申请用户ID */
    private Long userId;

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

    /** 审核状态：0待审核 1通过 2驳回 */
    private Integer applyStatus;

    /** 审核备注 */
    private String auditRemark;

    /** 逻辑删除 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
