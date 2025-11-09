package com.aftourism.admin.mapper;

import com.aftourism.admin.model.OperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper that persists operation logs.
 */
@Mapper
public interface OperationLogMapper {

    @Insert("INSERT INTO operation_log(username, path, method, params, result, duration, ip, is_deleted, create_time, update_time) "
            + "VALUES(#{username}, #{path}, #{method}, #{params}, #{result}, #{duration}, #{ip}, 0, NOW(), NOW())")
    int insert(OperationLog log);
}
