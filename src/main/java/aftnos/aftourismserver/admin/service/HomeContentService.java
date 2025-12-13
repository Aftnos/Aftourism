package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.HomeContentSaveDTO;
import aftnos.aftourismserver.admin.vo.HomeContentAdminVO;

/**
 * 后台首页内容管理服务。
 */
public interface HomeContentService {

    /**
     * 查询当前首页配置，包含简介与轮播图。
     */
    HomeContentAdminVO queryContent();

    /**
     * 保存首页配置（简介+轮播）。
     */
    void saveContent(HomeContentSaveDTO dto);
}
