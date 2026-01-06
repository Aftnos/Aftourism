package aftnos.aftourismserver.auth.dto;

import lombok.Builder;
import lombok.Value;

/**
 * 门户用户信息响应对象。
 */
@Value
@Builder
public class PortalUserInfoResponse {
    /** 用户主键 ID。 */
    Long userId;
    /** 登录账号/用户名。 */
    String userName;
    /** 真实姓名/昵称。 */
    String nickName;
    /** 手机号码。 */
    String phone;
    /** 头像地址。 */
    String avatar;
    /** 男女性别。 */
    String gender;
    /** 邮箱地址。 */
    String email;
    /** 用户备注。 */
    String remark;
    /** 是否为高级用户。 */
    Boolean advancedUser;
    /** 资质申请状态：NONE/PENDING/APPROVED/REJECTED */
    String qualificationStatus;
    /** 资质审核备注。 */
    String qualificationRemark;
}
