package aftnos.aftourismserver.monitor.mapper;

import aftnos.aftourismserver.monitor.pojo.SystemMetric;
import aftnos.aftourismserver.monitor.dto.SystemMetricPageQuery;
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

    /**
     * 条件分页查询系统监控记录
     *
     * @param query 查询参数
     * @return 记录列表
     */
    java.util.List<SystemMetric> selectPage(SystemMetricPageQuery query);
}
