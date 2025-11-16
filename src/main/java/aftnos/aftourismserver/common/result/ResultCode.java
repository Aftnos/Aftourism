package aftnos.aftourismserver.common.result;

import lombok.Getter;

/**
 * 响应结果码枚举
 * 定义系统中通用的响应状态码
 */
@Getter
public enum ResultCode {

    // 通用成功/失败状态码
    /**
     * 成功状态码
     */
    SUCCESS(1, "success"),

    /**
     * 失败状态码
     */
    FAILURE(0, "failure"),

    // 业务相关状态码 (1001-1999)
    /**
     * 参数错误
     */
    PARAM_ERROR(1001, "请求参数错误"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(1002, "权限不足"),

    /**
     * 用户未登录
     */
    NOT_LOGIN(1003, "用户未登录"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(1004, "数据不存在"),

    /**
     * 系统内部错误
     */
    INTERNAL_ERROR(1005, "系统内部错误"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(1006, "请求超时"),

    /**
     * 数据已存在
     */
    DATA_EXISTS(1007, "数据已存在"),

    /**
     * 数据被使用无法修改
     */
    DATA_USED(1008, "数据被使用无法修改"),

    /**
     * 数据不完整
     */
    DATA_INCOMPLETE(1009, "请求数据不完整"),

    /**
     * 数据不规范
     */
    DATA_INCORRECT(1010, "请求数据不规范"),

    /**
     * 业务异常
     */
    BUSINESS_EXCEPTION(1011, "业务异常"),

    /**
     * 请求路径错误
     */
    PATH_ERROR(1012, "请求路径错误"),
    ;

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态信息
     */
    private final String msg;

    /**
     * 构造方法
     *
     * @param code 状态码
     * @param msg  状态信息
     */
    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}