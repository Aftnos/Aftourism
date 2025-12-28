package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.OperationLogPageQuery;
import aftnos.aftourismserver.admin.mapper.OperationLogMapper;
import aftnos.aftourismserver.admin.pojo.OperationLog;
import aftnos.aftourismserver.admin.service.OperationLogService;
import aftnos.aftourismserver.admin.vo.OperationLogVO;
import aftnos.aftourismserver.common.vo.PageResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志管理服务实现。
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    public OperationLogServiceImpl(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    @Override
    public PageResponse<OperationLogVO> page(OperationLogPageQuery query) {
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<OperationLog> logs = operationLogMapper.search(query);
        PageInfo<OperationLog> pageInfo = new PageInfo<>(logs);
        List<OperationLogVO> records = logs.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return new PageResponse<>(records, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
    }

    private OperationLogVO convert(OperationLog log) {
        OperationLogVO vo = new OperationLogVO();
        vo.setId(log.getId());
        vo.setOperatorId(log.getOperatorId());
        vo.setOperatorType(log.getOperatorType());
        vo.setModuleName(log.getModuleName());
        vo.setOperationName(log.getOperationName());
        vo.setRequestUri(log.getRequestUri());
        vo.setRequestMethod(log.getRequestMethod());
        vo.setSuccessFlag(log.getSuccessFlag());
        vo.setErrorMsg(log.getErrorMsg());
        vo.setCostMs(log.getCostMs());
        vo.setIpAddress(log.getIpAddress());
        vo.setCreateTime(log.getCreateTime());
        return vo;
    }
}
