package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员实体，对应数据库表 {@code t_admin}。
 */
@Data
public class Admin {

    /** 主键ID */
    private Long id;
    /** 管理员账号 */
    private String username;
    /** 登录密码（BCrypt 加密） */
    private String password;
    /** 真实姓名 */
    private String realName;
    /** 联系电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 头像地址 */
    private String avatar;
    /** 个人介绍 */
    private String introduction;
    /** 角色编码，多个角色使用英文逗号分隔 */
    private String roleCode;
    /** 是否超级管理员：1是 0否 */
    private Integer isSuper;
    /** 启用状态：1启用 0禁用 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 逻辑删除标记：0未删除 1已删除 */
    private Integer isDeleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
