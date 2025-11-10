package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.NewsDTO;
import aftnos.aftourismserver.admin.dto.NewsPageQuery;
import aftnos.aftourismserver.admin.vo.NewsVO;
import com.github.pagehelper.PageInfo;

public interface NewsService {

    Long createNews(NewsDTO newsDTO);

    void updateNews(Long id, NewsDTO newsDTO);

    void deleteNews(Long id);

    PageInfo<NewsVO> pageNews(NewsPageQuery query);
}
