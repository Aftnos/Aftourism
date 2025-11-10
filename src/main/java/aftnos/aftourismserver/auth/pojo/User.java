package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 对应数据库 t_user 表的实体对象
 */
@Data
public class User {

    /** 主键ID */
    private Long id;

    /** 登录账号 */
    private String username;

    /** 加密后的登录密码 */
    private String password;

    /** 用户昵称/姓名 */
    private String nickname;

    /** 联系电话 */
    private String phone;

    /** 邮箱地址 */
    private String email;

    /** 头像地址 */
    private String avatar;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 备注信息 */
    private String remark;

    /** 逻辑删除标记：0否 1是 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
