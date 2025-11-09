package com.aftourism.monitor.mapper;

import com.aftourism.monitor.pojo.SiteVisitStats;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 访问统计 Mapper。
 */
@Mapper
public interface SiteVisitStatsMapper {

    List<SiteVisitStats> selectRecent(int limit);
}
