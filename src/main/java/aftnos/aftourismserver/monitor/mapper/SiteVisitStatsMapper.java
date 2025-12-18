package aftnos.aftourismserver.monitor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

import aftnos.aftourismserver.monitor.pojo.SiteVisitStats;

/**
 * 站点访问统计 Mapper
 */
@Mapper
public interface SiteVisitStatsMapper {

    /**
     * 按日累计访问统计数据
     *
     * @param statDate    统计日期
     * @param pvIncrement PV 增量
     * @param uvIncrement UV 增量
     * @param ipIncrement IP 增量
     * @return 影响行数
     */
    int upsertVisitStats(@Param("statDate") LocalDate statDate,
                         @Param("pvIncrement") long pvIncrement,
                         @Param("uvIncrement") long uvIncrement,
                         @Param("ipIncrement") long ipIncrement);

    /**
     * 查询指定日期范围内的访问记录
     */
    List<SiteVisitStats> selectByDateRange(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    /**
     * 汇总指定日期范围内的 PV/UV/IP
     */
    SiteVisitStats sumByDateRange(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);
}
