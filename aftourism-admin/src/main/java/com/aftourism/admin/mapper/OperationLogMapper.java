package com.aftourism.admin.mapper;

import com.aftourism.admin.pojo.OperationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作日志数据访问。
 */
@Mapper
public interface OperationLogMapper {

    void insert(OperationLog log);

    List<OperationLog> selectLatest();
}
