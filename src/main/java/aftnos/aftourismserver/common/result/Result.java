package aftnos.aftourismserver.common.result;

import lombok.Data;

/**
 * 后端统一返回结果
 */
@Data
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    /**
     * 构建成功响应（无数据）。
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = ResultCode.SUCCESS.getCode();
        result.msg = ResultCode.SUCCESS.getMsg();
        return result;
    }

    /**
     * 构建成功响应，并携带数据。
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.data = data;
        result.code = ResultCode.SUCCESS.getCode();
        result.msg = ResultCode.SUCCESS.getMsg();
        return result;
    }

    /**
     * 构建自定义成功响应。
     */
    public static <T> Result<T> success(T data, String msg) {
        Result<T> result = new Result<>();
        result.data = data;
        result.code = ResultCode.SUCCESS.getCode();
        result.msg = msg;
        return result;
    }

    /**
     * 构建错误响应，使用统一错误码。
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.code = resultCode.getCode();
        result.msg = resultCode.getMsg();
        return result;
    }

    /**
     * 构建带自定义消息的错误响应。
     */
    public static <T> Result<T> error(ResultCode resultCode, String msg) {
        Result<T> result = new Result<>();
        result.code = resultCode.getCode();
        result.msg = msg;
        return result;
    }
}
