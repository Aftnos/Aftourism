package com.aftourism.admin.mapper;

import com.aftourism.admin.pojo.SystemMetric;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统监控指标访问层。
 */
@Mapper
public interface SystemMetricMapper {

    List<SystemMetric> selectLatest();
}
