package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.NewsPortalPageQuery;
import aftnos.aftourismserver.portal.vo.NewsPortalVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户新闻业务接口
 */
public interface NewsPortalService {

    /**
     * 分页查询门户新闻列表
     *
     * @param query 查询参数
     * @return 新闻分页信息
     */
    PageInfo<NewsPortalVO> pageNews(NewsPortalPageQuery query);

    /**
     * 查询门户新闻详情
     *
     * @param id 新闻ID
     * @return 新闻详情
     */
    NewsPortalVO getNewsDetail(Long id);
}
