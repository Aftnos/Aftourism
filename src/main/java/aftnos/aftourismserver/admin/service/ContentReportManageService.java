package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.ContentReportManageDTO;
import aftnos.aftourismserver.admin.dto.ContentReportManagePageQuery;
import aftnos.aftourismserver.admin.vo.ContentReportManageVO;
import com.github.pagehelper.PageInfo;

/**
 * 举报管理接口
 */
public interface ContentReportManageService {

    PageInfo<ContentReportManageVO> page(ContentReportManagePageQuery query);

    void update(Long id, ContentReportManageDTO dto);
}
