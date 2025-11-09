package com.aftourism.common.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * Value object representing an operation log entry before persistence.
 */
@Data
@Builder
public class OperationLogEntry {
    private String username;
    private String path;
    private String method;
    private String params;
    private String result;
    private Long duration;
    private String ip;
    private LocalDateTime createTime;
}
