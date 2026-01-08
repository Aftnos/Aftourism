package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ExchangeCommentUpdateDTO;
import aftnos.aftourismserver.portal.dto.ExchangeCommentPageQuery;
import aftnos.aftourismserver.portal.vo.ExchangeCommentVO;
import com.github.pagehelper.PageInfo;

/**
 * 交流评论后台管理服务
 */
public interface ExchangeCommentManageService {

    /**
     * 分页查询评论
     */
    PageInfo<ExchangeCommentVO> page(Long articleId, ExchangeCommentPageQuery query);

    /**
     * 更新评论内容
     */
    void update(Long commentId, ExchangeCommentUpdateDTO dto);

    /**
     * 删除评论
     */
    void delete(Long commentId);
}
