package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 最新注册门户用户展示对象
 */
@Data
public class PortalNewUserVO {
    private Long id;
    private String username;
    private String nickname;
    private String gender;
    private String phone;
    private String email;
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
