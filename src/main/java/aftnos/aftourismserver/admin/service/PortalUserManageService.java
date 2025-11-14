package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.PortalUserPageQuery;
import aftnos.aftourismserver.admin.dto.PortalUserUpdateRequest;
import aftnos.aftourismserver.admin.vo.PortalUserVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户用户后台管理服务。
 */
public interface PortalUserManageService {

    /** 分页查询门户用户 */
    PageInfo<PortalUserVO> page(PortalUserPageQuery query);

    /** 更新角色或状态 */
    void update(Long id, PortalUserUpdateRequest request);
}
