package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.dto.UserInfoResponse;

/**
 * 管理端用户信息查询服务。
 */
public interface AdminUserInfoService {

    /**
     * 获取当前登录管理员信息。
     *
     * @return 管理端用户信息响应对象
     */
    UserInfoResponse getCurrentAdminInfo();
}
