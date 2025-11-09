package com.aftourism.admin.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 管理员登录成功返回信息。
 */
@Data
@Builder
public class AdminLoginVO {

    /** 管理员ID */
    private Long adminId;

    /** 登录账号 */
    private String username;

    /** 显示名称 */
    private String realName;

    /** 登录令牌 */
    private String token;
}
