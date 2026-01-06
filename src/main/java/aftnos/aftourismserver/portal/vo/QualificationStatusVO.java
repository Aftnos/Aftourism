package aftnos.aftourismserver.portal.vo;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 门户资质申请状态视图对象。
 */
@Value
@Builder
public class QualificationStatusVO {
    /** 资质状态：NONE/PENDING/APPROVED/REJECTED */
    String status;
    /** 审核备注 */
    String auditRemark;
    /** 提交时间 */
    LocalDateTime applyTime;
}
