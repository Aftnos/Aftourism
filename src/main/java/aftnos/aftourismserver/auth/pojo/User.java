package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 前台用户实体，对应数据库 t_user 表。
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
