package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志列表展示对象。
 */
@Data
public class OperationLogVO {

    /** 主键ID */
    private Long id;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人类型 */
    private String operatorType;

    /** 模块名称 */
    private String moduleName;

    /** 操作名称 */
    private String operationName;

    /** 请求URI */
    private String requestUri;

    /** 请求方法 */
    private String requestMethod;

    /** 是否成功 */
    private Boolean successFlag;

    /** 错误信息 */
    private String errorMsg;

    /** 请求耗时 */
    private Integer costMs;

    /** 请求IP */
    private String ipAddress;

    /** 创建时间 */
    private LocalDateTime createTime;
}
