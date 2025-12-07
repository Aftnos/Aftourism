package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.dto.UserInfoResponse;

/**
 * 用户信息查询服务，负责从登录上下文中提取用户信息。
 */
public interface UserInfoService {

    /**
     * 获取当前登录用户的信息。
     *
     * @return 用户信息响应对象
     */
    UserInfoResponse getCurrentUserInfo();
}
