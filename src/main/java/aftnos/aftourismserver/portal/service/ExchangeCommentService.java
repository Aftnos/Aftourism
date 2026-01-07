package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.ExchangeCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.ExchangeCommentPageQuery;
import aftnos.aftourismserver.portal.vo.ExchangeCommentVO;
import com.github.pagehelper.PageInfo;

/**
 * 交流评论业务接口
 */
public interface ExchangeCommentService {

    Long addComment(Long articleId, ExchangeCommentCreateDTO dto, Long userId);

    PageInfo<ExchangeCommentVO> pageComments(Long articleId, ExchangeCommentPageQuery query);

    void likeComment(Long commentId, Long userId);

    void deleteOwnComment(Long commentId, Long userId);
}
