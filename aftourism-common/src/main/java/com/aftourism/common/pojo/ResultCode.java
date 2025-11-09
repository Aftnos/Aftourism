package com.aftourism.common.pojo;

import lombok.Getter;

/**
 * 统一状态码定义。
 */
@Getter
public enum ResultCode {
    /** 成功 */
    SUCCESS("00000", "操作成功"),
    /** 参数错误 */
    BAD_REQUEST("A0400", "请求参数不合法"),
    /** 未授权 */
    UNAUTHORIZED("A0301", "登录状态失效"),
    /** 无权限 */
    FORBIDDEN("A0303", "无权限访问"),
    /** 资源不存在 */
    NOT_FOUND("A0404", "资源不存在"),
    /** 业务异常 */
    BUSINESS_ERROR("B0001", "业务处理失败"),
    /** 系统错误 */
    SYSTEM_ERROR("C0001", "系统繁忙，请稍后重试");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
