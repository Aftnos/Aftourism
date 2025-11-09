package com.aftourism.admin.logging;

import com.aftourism.admin.mapper.OperationLogMapper;
import com.aftourism.admin.model.OperationLog;
import com.aftourism.common.logging.OperationLogRecorder;
import com.aftourism.common.model.OperationLogEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Persists operation logs into the operation_log table.
 */
@Component
@RequiredArgsConstructor
public class DatabaseOperationLogRecorder implements OperationLogRecorder {

    private final OperationLogMapper operationLogMapper;

    @Override
    @Async
    public void record(OperationLogEntry entry) {
        OperationLog log = new OperationLog();
        log.setUsername(entry.getUsername());
        log.setPath(entry.getPath());
        log.setMethod(entry.getMethod());
        log.setParams(entry.getParams());
        log.setResult(entry.getResult());
        log.setDuration(entry.getDuration() == null ? null : entry.getDuration().intValue());
        log.setIp(entry.getIp());
        operationLogMapper.insert(log);
    }
}
