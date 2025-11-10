package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.pojo.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Mapper接口
 */
@Mapper
public interface OperationLogMapper {
    
    /**
     * 插入操作日志
     * @param operationLog 操作日志实体
     * @return 插入记录数
     */
    int insert(OperationLog operationLog);
}