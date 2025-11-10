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

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = ResultCode.SUCCESS.getCode();
        result.msg = "success";
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.data = data;
        result.code = ResultCode.SUCCESS.getCode();
        result.msg = "success";
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = ResultCode.FAILURE.getCode();
        result.msg = msg;
        return result;
    }
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.code = resultCode.getCode();
        result.msg = resultCode.getMsg();
        return result;
    }
    public static <T> Result<T> error(ResultCode resultCode,String msg) {
        Result<T> result = new Result<>();
        result.code = resultCode.getCode();
        result.msg = msg;
        return result;
    }
}