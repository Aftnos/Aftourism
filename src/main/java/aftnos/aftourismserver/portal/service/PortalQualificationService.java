package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.QualificationApplyRequest;
import aftnos.aftourismserver.portal.vo.QualificationStatusVO;

/**
 * 门户用户资质申请服务接口。
 */
public interface PortalQualificationService {

    /**
     * 提交资质申请。
     *
     * @param userId 申请用户ID
     * @param request 申请请求
     */
    void submit(Long userId, QualificationApplyRequest request);

    /**
     * 获取最新资质申请状态。
     *
     * @param userId 用户ID
     * @return 资质状态信息
     */
    QualificationStatusVO latestStatus(Long userId);
}
