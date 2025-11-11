package aftnos.aftourismserver.monitor.mapper;

import aftnos.aftourismserver.monitor.pojo.SystemMetric;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统监控指标 Mapper
 */
@Mapper
public interface SystemMetricMapper {

    /**
     * 插入系统监控记录
     *
     * @param metric 指标实体
     * @return 影响行数
     */
    int insert(SystemMetric metric);
}
