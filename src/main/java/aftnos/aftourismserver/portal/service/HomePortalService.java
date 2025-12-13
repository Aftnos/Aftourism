package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.vo.HomePortalVO;

/**
 * 门户首页展示服务。
 */
public interface HomePortalService {

    /**
     * 聚合查询首页轮播与简介。
     */
    HomePortalVO loadHomeContent();
}
