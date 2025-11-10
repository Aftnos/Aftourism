package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 对应数据库表: t_operation_log
 * 
 * 由于MyBatis已开启驼峰命名自动转换(map-underscore-to-camel-case: true)
 * 数据库中的下划线字段会自动映射到实体类的驼峰属性
 * 例如: trace_id -> traceId, operator_id -> operatorId
 */
@Data
public class OperationLog {
    // 主键
    private Long id;
    
    // 链路ID/追踪ID
    private String traceId;
    
    // 操作人ID（管理员或用户）
    private Long operatorId;
    
    // 操作者类型：ADMIN/USER/SYSTEM
    private String operatorType;
    
    // 模块名称：如新闻管理/活动管理
    private String moduleName;
    
    // 操作名称：新增新闻/删除活动等
    private String operationName;
    
    // 请求URI
    private String requestUri;
    
    // HTTP方法：GET/POST等
    private String requestMethod;
    
    // 执行的类方法签名
    private String classMethod;
    
    // 请求参数JSON
    private String requestParams;
    
    // 响应结果摘要（可选）
    private String responseBody;
    
    // 是否成功：1成功 0失败
    private Boolean successFlag;
    
    // 错误信息
    private String errorMsg;
    
    // 接口耗时（毫秒）
    private Integer costMs;
    
    // 请求IP
    private String ipAddress;
    
    // User-Agent
    private String userAgent;
    
    // 逻辑删除：0否 1是
    private Boolean deleted;
    
    // 创建时间
    private LocalDateTime createTime;
    
    // 更新时间
    private LocalDateTime updateTime;
}