package com.aftourism.common.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一接口返回结果封装。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 是否成功 */
    private boolean success;

    /** 状态码 */
    private String code;

    /** 提示消息 */
    private String message;

    /** 返回数据 */
    private T data;

    /**
     * 成功返回。
     */
    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功但无数据返回。
     */
    public static <T> ResponseResult<T> ok() {
        return ok(null);
    }

    /**
     * 失败返回。
     */
    public static <T> ResponseResult<T> fail(String code, String message) {
        return new ResponseResult<>(false, code, message, null);
    }

    /**
     * 使用预设枚举失败返回。
     */
    public static <T> ResponseResult<T> fail(ResultCode resultCode) {
        return fail(resultCode.getCode(), resultCode.getMessage());
    }
}
