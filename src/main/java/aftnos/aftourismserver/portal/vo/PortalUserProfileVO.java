package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户用户主页信息
 */
@Data
public class PortalUserProfileVO {
    private Long userId;
    private String userName;
    private String nickName;
    private String avatar;
    private String gender;
    private String remark;
    private Boolean advancedUser;
    private String qualificationStatus;
    private LocalDateTime createTime;
}
