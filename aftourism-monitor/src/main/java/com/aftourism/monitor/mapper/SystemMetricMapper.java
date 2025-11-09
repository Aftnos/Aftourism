package com.aftourism.monitor.mapper;

import com.aftourism.monitor.pojo.SystemMetric;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统指标 Mapper。
 */
@Mapper
public interface SystemMetricMapper {

    List<SystemMetric> selectLatest();
}
