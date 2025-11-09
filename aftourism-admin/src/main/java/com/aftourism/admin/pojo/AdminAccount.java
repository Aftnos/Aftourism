package com.aftourism.admin.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

/**
 * 管理员实体，对应 t_admin 表。
 */
@Data
public class AdminAccount extends BaseEntity {

    /** 管理员账号 */
    private String username;

    /** 登录密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 备注 */
    private String remark;
}
