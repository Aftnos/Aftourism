package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.AdminPasswordUpdateRequest;
import aftnos.aftourismserver.admin.dto.AdminProfileUpdateRequest;
import aftnos.aftourismserver.admin.vo.AdminProfileVO;

/**
 * 管理员个人中心服务。
 */
public interface AdminProfileService {

    /**
     * 获取当前管理员的个人资料。
     *
     * @return 管理员资料
     */
    AdminProfileVO currentProfile();

    /**
     * 更新当前管理员的个人资料。
     *
     * @param request 更新请求体
     */
    void updateProfile(AdminProfileUpdateRequest request);

    /**
     * 更新当前管理员的密码。
     *
     * @param request 密码更新请求体
     */
    void updatePassword(AdminPasswordUpdateRequest request);
}
