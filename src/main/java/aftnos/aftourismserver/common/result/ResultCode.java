package aftnos.aftourismserver.common.result;

import lombok.Getter;

/**
 * 统一的响应状态码定义。
 * <p>按照登录接口文档中的状态码约定，对应 HTTP 语义进行划分，
 * 其余业务场景也复用这些标准状态码，便于前后端协作。</p>
 */
@Getter
public enum ResultCode {

    /** 成功 */
    SUCCESS(200, "成功"),

    /** 客户端请求错误 */
    BAD_REQUEST(400, "错误"),

    /** 请求缺少合法认证 */
    UNAUTHORIZED(401, "未授权"),

    /** 已认证但无权访问 */
    FORBIDDEN(403, "禁止访问"),

    /** 资源不存在 */
    NOT_FOUND(404, "未找到"),

    /** 请求方法不被允许 */
    METHOD_NOT_ALLOWED(405, "方法不允许"),

    /** 请求超时 */
    REQUEST_TIMEOUT(408, "请求超时"),

    /** 服务器内部错误 */
    INTERNAL_SERVER_ERROR(500, "服务器错误"),

    /** 功能未实现 */
    NOT_IMPLEMENTED(501, "未实现"),

    /** 网关错误 */
    BAD_GATEWAY(502, "网关错误"),

    /** 服务不可用 */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /** 网关超时 */
    GATEWAY_TIMEOUT(504, "网关超时"),

    /** HTTP 版本不支持 */
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP版本不支持"),

    /** 请求参数不合法，兼容原有校验逻辑 */
    DATA_INCORRECT(400, "请求数据不规范"),

    /** 请求数据缺失 */
    DATA_INCOMPLETE(400, "请求数据不完整"),

    /** 权限不足 */
    PERMISSION_DENIED(403, "权限不足"),

    /** 未登录或 token 失效 */
    NOT_LOGIN(401, "未授权"),

    /** 路径不存在 */
    PATH_ERROR(404, "未找到"),

    /** 数据被占用 */
    DATA_USED(400, "请求冲突"),

    /** 上传文件类型不支持 */
    UPLOAD_FILE_TYPE_NOT_ALLOWED(400, "上传文件类型不允许"),

    /** 业务异常统一出口 */
    BUSINESS_EXCEPTION(400, "错误"),

    /** 账户登录异常 */
    ACCOUNT_LOGIN_EXCEPTION(1001, "账户登录异常"),

    /** 客户端连接中断 */
    CLIENT_CONNECTION_INTERRUPTED(499, "客户端连接中断"),
    ;

    /** 状态码 */
    private final int code;

    /** 状态描述 */
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
