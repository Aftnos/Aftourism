package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.NewsDTO;
import aftnos.aftourismserver.admin.dto.NewsPageQuery;
import aftnos.aftourismserver.admin.vo.NewsVO;
import com.github.pagehelper.PageInfo;

/**
 * 后台管理模块的新闻业务接口，定义新闻增删改查与分页查询能力。
 */
public interface NewsService {

    Long createNews(NewsDTO newsDTO);

    void updateNews(Long id, NewsDTO newsDTO);

    void deleteNews(Long id);

    PageInfo<NewsVO> pageNews(NewsPageQuery query);
}
