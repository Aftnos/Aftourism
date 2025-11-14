package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员账户展示对象。
 */
@Data
public class AdminAccountVO {

    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    private Boolean superAdmin;
    private String remark;
    private List<String> roleCodes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
