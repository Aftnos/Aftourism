package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ExchangeArticleAuditDTO;
import aftnos.aftourismserver.admin.dto.ExchangeArticleManagePageQuery;
import aftnos.aftourismserver.admin.vo.ExchangeArticleManageVO;
import com.github.pagehelper.PageInfo;

/**
 * 交流文章后台管理接口
 */
public interface ExchangeArticleManageService {

    PageInfo<ExchangeArticleManageVO> page(ExchangeArticleManagePageQuery query);

    ExchangeArticleManageVO detail(Long id);

    void audit(Long id, ExchangeArticleAuditDTO dto);
}
