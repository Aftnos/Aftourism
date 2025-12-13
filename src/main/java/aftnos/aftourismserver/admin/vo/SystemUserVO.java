package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户列表展示对象，对齐前端 SystemManage.UserListItem 结构。
 */
@Data
public class SystemUserVO {

    /** 主键 ID */
    private Long id;

    /** 头像地址 */
    private String avatar;

    /** 状态字符串，前端期望字符串类型 */
    private String status;

    /** 用户名 */
    private String userName;

    /** 性别-暂未实现-占位 */
    private String userGender;

    /** 昵称 */
    private String nickName;

    /** 手机号 */
    private String userPhone;

    /** 邮箱 */
    private String userEmail;

    /** 角色集合 */
    private List<String> userRoles;

    /** 创建人，暂无审计字段默认系统 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新人，暂无审计字段默认系统 */
    private String updateBy;

    /** 更新时间 */
    private LocalDateTime updateTime;
    
    /** 备注信息 */
    private String remark;
}