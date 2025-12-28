package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.pojo.OperationLog;
import aftnos.aftourismserver.admin.dto.OperationLogPageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 分页查询操作日志。
     *
     * @param query 查询条件
     * @return 操作日志列表
     */
    List<OperationLog> search(@Param("query") OperationLogPageQuery query);
}
