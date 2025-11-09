package com.aftourism.admin.mapper;

import com.aftourism.admin.pojo.SiteVisitStats;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 访问统计数据访问层。
 */
@Mapper
public interface SiteVisitStatsMapper {

    List<SiteVisitStats> selectRecent(int limit);
}
