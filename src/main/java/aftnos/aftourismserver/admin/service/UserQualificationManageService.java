package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.QualificationPageQuery;
import aftnos.aftourismserver.admin.vo.QualificationApplyManageVO;
import com.github.pagehelper.PageInfo;

/**
 * 管理端资质审核服务接口。
 */
public interface UserQualificationManageService {

    /**
     * 资质申请分页查询。
     *
     * @param query 查询参数
     * @return 分页结果
     */
    PageInfo<QualificationApplyManageVO> page(QualificationPageQuery query);

    /**
     * 资质申请详情。
     *
     * @param applyId 申请ID
     * @return 详情信息
     */
    QualificationApplyManageVO detail(Long applyId);

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
