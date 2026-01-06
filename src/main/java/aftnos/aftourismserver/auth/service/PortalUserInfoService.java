package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.dto.PortalUserInfoResponse;
import aftnos.aftourismserver.auth.dto.UserInfoUpdateRequest;

/**
 * 门户用户信息服务接口。
 */
public interface PortalUserInfoService {

    /**
     * 获取当前登录的门户用户信息。
     *
     * @return 门户用户信息响应对象
     */
    PortalUserInfoResponse getCurrentPortalUserInfo();

    /**
     * 更新当前登录门户用户的个人资料。
     *
     * @param request 更新请求体
     */
    void updateCurrentUserInfo(UserInfoUpdateRequest request);
}
