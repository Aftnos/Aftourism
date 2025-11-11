package aftnos.aftourismserver.monitor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

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
}
