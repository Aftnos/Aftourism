package com.aftourism.user.entity;

import com.aftourism.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * System user entity representing both tourists and administrators.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    private String username;
    private String password;
    private String role;
}
