package com.aftourism.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * Base entity holding audit fields shared across tables.
 */
@Data
public abstract class BaseEntity implements Serializable {

    private Long id;

    private Boolean isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
