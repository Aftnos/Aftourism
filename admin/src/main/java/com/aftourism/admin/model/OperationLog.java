package com.aftourism.admin.model;

import com.aftourism.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity mapping to the operation_log table.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLog extends BaseEntity {

    private String username;
    private String path;
    private String method;
    private String params;
    private String result;
    private Integer duration;
    private String ip;
}
