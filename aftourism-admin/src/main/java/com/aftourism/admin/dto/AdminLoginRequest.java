package com.aftourism.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 管理员登录请求参数。
 */
@Data
public class AdminLoginRequest {

    /** 账号 */
    @NotBlank(message = "账号不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
}
