package com.aftourism.common.logging;

import com.aftourism.common.model.OperationLogEntry;

/**
 * Strategy interface allowing different modules to persist operation logs.
 */
public interface OperationLogRecorder {

    void record(OperationLogEntry entry);
}
