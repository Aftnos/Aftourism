package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.NoticePortalPageQuery;
import aftnos.aftourismserver.portal.vo.NoticeDetailVO;
import aftnos.aftourismserver.portal.vo.NoticeSummaryVO;
import com.github.pagehelper.PageInfo;

/**
 * 门户通知公告业务接口
 */
public interface NoticePortalService {

    /**
     * 分页查询通知公告
     */
    PageInfo<NoticeSummaryVO> pageNotices(NoticePortalPageQuery query);

    /**
     * 查看通知公告详情
     */
    NoticeDetailVO getNoticeDetail(Long id);
}
