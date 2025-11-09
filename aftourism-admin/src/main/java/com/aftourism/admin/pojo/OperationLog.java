package com.aftourism.admin.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

/**
 * 操作日志实体，对应 t_operation_log 表。
 */
@Data
public class OperationLog extends BaseEntity {

    private String traceId;
    private Long operatorId;
    private String operatorType;
    private String moduleName;
    private String operationName;
    private String requestUri;
    private String requestMethod;
    private String classMethod;
    private String requestParams;
    private String responseBody;
    private Integer successFlag;
    private String errorMsg;
    private Integer costMs;
    private String ipAddress;
    private String userAgent;
}
