package aftnos.aftourismserver.portal.service;

import aftnos.aftourismserver.portal.dto.ContentReportCreateDTO;

/**
 * 举报业务接口
 */
public interface ContentReportService {

    Long createReport(Long userId, ContentReportCreateDTO dto);
}
