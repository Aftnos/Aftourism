package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.ExchangeArticleCreateDTO;
import aftnos.aftourismserver.portal.dto.ExchangeArticlePageQuery;
import aftnos.aftourismserver.portal.vo.ExchangeArticleVO;
import com.github.pagehelper.PageInfo;

/**
 * 交流文章业务接口
 */
public interface ExchangeArticleService {

    Long createArticle(Long userId, ExchangeArticleCreateDTO dto);

    PageInfo<ExchangeArticleVO> pageArticles(ExchangeArticlePageQuery query, Long userId, boolean includeAll);

    ExchangeArticleVO getDetail(Long id, Long viewerId);

    void likeArticle(Long articleId, Long userId);
}
