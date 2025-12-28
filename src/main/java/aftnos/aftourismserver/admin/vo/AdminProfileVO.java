package aftnos.aftourismserver.admin.vo;

import lombok.Data;

/**
 * 管理员个人中心信息返回对象。
 */
@Data
public class AdminProfileVO {

    /** 管理员ID */
    private Long id;

    /** 管理员账号 */
    private String username;

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
}
