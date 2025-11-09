package com.aftourism.admin.service;

import com.aftourism.admin.dto.AdminLoginRequest;
import com.aftourism.admin.vo.AdminLoginVO;

/**
 * 管理员认证服务接口。
 */
public interface AdminAuthService {

    /**
     * 管理员登录逻辑。
     */
    AdminLoginVO login(AdminLoginRequest request);

    /**
     * 管理员登出逻辑。
     */
    void logout(Long adminId);
}
