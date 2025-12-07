package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.vo.MenuRouteVO;

import java.util.List;

/**
 * 菜单查询服务，负责基于 RBAC 返回前端可用的菜单路由树。
 */
public interface MenuQueryService {

    /**
     * 读取当前登录用户可见的菜单树。
     *
     * @return 符合前端定义的路由列表
     */
    List<MenuRouteVO> loadCurrentUserMenus();
}
