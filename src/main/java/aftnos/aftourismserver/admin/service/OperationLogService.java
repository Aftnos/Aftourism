package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.OperationLogPageQuery;
import aftnos.aftourismserver.admin.vo.OperationLogVO;
import aftnos.aftourismserver.common.vo.PageResponse;

/**
 * 操作日志管理服务。
 */
public interface OperationLogService {

    /**
     * 分页查询操作日志。
     *
     * @param query 查询参数
     * @return 分页数据
     */
    PageResponse<OperationLogVO> page(OperationLogPageQuery query);
}
