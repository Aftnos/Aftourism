package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.NoticeDTO;
import aftnos.aftourismserver.admin.dto.NoticePageQuery;
import aftnos.aftourismserver.admin.vo.NoticeVO;
import com.github.pagehelper.PageInfo;

/**
 * 通知公告业务接口
 */
public interface NoticeService {

    /**
     * 新增通知公告
     */
    Long createNotice(NoticeDTO noticeDTO);

    /**
     * 修改通知公告
     */
    void updateNotice(Long id, NoticeDTO noticeDTO);

    /**
     * 逻辑删除通知公告
     */
    void deleteNotice(Long id);

    /**
     * 分页查询通知公告
     */
    PageInfo<NoticeVO> pageNotices(NoticePageQuery query);
}
