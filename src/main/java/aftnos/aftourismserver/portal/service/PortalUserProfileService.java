package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.vo.PortalUserProfileVO;

/**
 * 门户用户主页服务
 */
public interface PortalUserProfileService {
    PortalUserProfileVO getProfile(Long userId);
}
