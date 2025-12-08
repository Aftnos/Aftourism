package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.SystemRoleSearchQuery;
import aftnos.aftourismserver.admin.dto.SystemUserSearchQuery;
import aftnos.aftourismserver.admin.vo.SystemRoleVO;
import aftnos.aftourismserver.admin.vo.SystemUserVO;
import aftnos.aftourismserver.common.vo.PageResponse;

/**
 * 系统管理（用户、角色、菜单）数据服务，对齐前端 system 模块接口。
 */
public interface SystemManageService {

    /**
     * 分页查询用户列表。
     */
    PageResponse<SystemUserVO> pageUsers(SystemUserSearchQuery query);

    /**
     * 分页查询角色列表。
     */
    PageResponse<SystemRoleVO> pageRoles(SystemRoleSearchQuery query);
}
