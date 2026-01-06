package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户用户后台展示对象。
 */
@Data
public class PortalUserVO {

    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private Integer status;
    private Integer isAdvanced;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
