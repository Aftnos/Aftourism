package aftnos.aftourismserver.admin.service;

/**
 * 管理端资质审核服务接口。
 */
public interface UserQualificationManageService {

    /**
     * 通过资质申请。
     *
     * @param applyId 申请ID
     * @param auditRemark 审核备注
     */
    void approve(Long applyId, String auditRemark);

    /**
     * 驳回资质申请。
     *
     * @param applyId 申请ID
     * @param auditRemark 审核备注
     */
    void reject(Long applyId, String auditRemark);
}
