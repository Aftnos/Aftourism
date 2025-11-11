package aftnos.aftourismserver.monitor.mapper;

import aftnos.aftourismserver.monitor.pojo.RedisBenchmarkRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * Redis 性能基准数据 Mapper
 */
@Mapper
public interface RedisBenchmarkMapper {

    /**
     * 写入 Redis 性能采样数据
     *
     * @param record 采样记录
     * @return 影响行数
     */
    int insert(RedisBenchmarkRecord record);
}
