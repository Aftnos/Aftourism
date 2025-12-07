package aftnos.aftourismserver.common.exception;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.result.ResultCode;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 用于统一处理系统中抛出的各种异常，提供一致的错误响应格式
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理参数验证异常（@Valid注解触发的验证失败）
     * 通常发生在请求参数使用@NotNull、@NotBlank等注解但校验不通过时
     *
     * @param ex MethodArgumentNotValidException异常对象
     * @return 包含具体验证失败信息的结果对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String msg = String.join("; ", messages);
        log.warn("参数验证失败: {}", msg);
        return Result.error(ResultCode.BAD_REQUEST, msg);
    }

    /**
     * 处理HTTP消息不可读异常
     * 通常发生在请求体格式不正确或缺失必需参数时
     *
     * @param e HttpMessageNotReadableException异常对象
     * @return 表示请求数据不完整的错误结果
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("请求没参数不合法: {}", e.getMessage());
        return Result.error(ResultCode.DATA_INCOMPLETE);
    }

    /**
     * 处理业务异常
     * 当系统业务逻辑出现预期中的错误情况时抛出此异常
     *
     * @param e BusinessException业务异常对象
     * @return 包含具体业务错误信息的结果对象
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(ResultCode.BUSINESS_EXCEPTION, e.getMessage());
    }

    /**
     * 上传了错误的文件
     * @param e IllegalArgumentException异常对象
     * @return 响应包含具体验证失败信息的结果对象
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("上传了不允许的文件: {}", e.getMessage());
        return Result.error(ResultCode.UPLOAD_FILE_TYPE_NOT_ALLOWED);
    }

    /**
     * 处理未授权异常
     * 当用户身份认证失败或token无效时抛出此异常
     * 返回401状态码表示需要重新认证
     *
     * @param e UnauthorizedException未授权异常对象
     * @return 包含认证失败信息的响应实体
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Result<String>> handleUnauthorizedException(UnauthorizedException e) {
        log.warn("鉴权失败: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Result.error(ResultCode.UNAUTHORIZED, e.getMessage()));
    }

    /**
     * 处理通用异常
     * 作为兜底的异常处理方法，处理所有未被专门处理的异常类型
     *
     * @param e Exception异常对象
     * @return 表示系统内部错误的结果对象
     */
    @ExceptionHandler
    public Result<String> error(Exception e){
        log.error("错误",e);
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理资源未找到异常
     * 当访问不存在的API端点时触发此异常
     * 返回404状态码表示请求的资源不存在
     *
     * @param e NoResourceFoundException异常对象
     * @return 表示请求路径错误的响应实体
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<String>> handleNoResourceFoundException(NoResourceFoundException e){
        log.error("请求路径错误: {}", e.getMessage());
        log.error("完整的异常信息: ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.error(ResultCode.NOT_FOUND));
    }

    /**
     * 处理权限不足异常
     * 当已认证用户尝试访问没有权限的资源时触发此异常
     *
     * @return 表示权限不足的错误结果
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Result<String>> handleAuthorizationDeniedException() {
        log.error("权限不足");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Result.error(ResultCode.FORBIDDEN));
    }

    /**
     * 处理重复键异常
     * 当数据库插入或更新操作违反唯一性约束时触发此异常
     * 例如：用户名、邮箱等唯一字段重复插入
     *
     * @param e DuplicateKeyException异常对象
     * @return 包含具体重复字段信息的错误结果
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<String> DuplicateKeyException(DuplicateKeyException e){
        log.error("错误",e);
        String message = e.getMessage();

        // 查找引号中的内容
        int start = message.indexOf("'");
        int end = message.indexOf("'", start + 1);
        String msg = message.substring(start + 1, end);
        return Result.error(ResultCode.BAD_REQUEST, "QAQ~，" + msg + " 已存在");
    }

    /**
     * 处理数据完整性违规异常
     * 当数据库操作违反完整性约束时触发此异常
     * 例如：非空字段传入null值
     *
     * @param e DataIntegrityViolationException异常对象
     * @return 包含具体字段约束信息的错误结果
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("数据完整性约束违反", e);
        String message = e.getMessage();

        // 使用正则表达式提取字段名
        Pattern pattern = Pattern.compile("Column '([^']+)' cannot be null");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String columnName = matcher.group(1);
            // 转换列名到更友好的格式（如需要）
            return Result.error(ResultCode.DATA_INCOMPLETE,"字段 '" + columnName + "' 不能为空");
        }

        return Result.error(ResultCode.DATA_INCOMPLETE);
    }

    /**
     * 处理运行时异常
     * 处理系统运行过程中发生的各种运行时错误
     * 特别处理了部门删除相关的业务异常
     *
     * @param e RuntimeException异常对象
     * @return 对应的错误结果
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        log.error("行时异常", e);
        String message = e.getMessage();

        // 检查是否是部门删除异常
        if (message != null && message.contains("该部门下存在员工，无法删除")) {
            return Result.error(ResultCode.DATA_USED);
        }

        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理空指针异常
     * 特别处理了JWT Token解析过程中的空指针异常
     * 如果是JWT相关的空指针异常，则返回401未认证状态
     *
     * @param e NullPointerException异常对象
     * @return 根据异常类型返回不同的响应实体
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Result<String>> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        // 特别处理JWT相关的空指针异常
        if (e.getMessage() != null && e.getMessage().contains("startsWith")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Result.error(ResultCode.UNAUTHORIZED));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(ResultCode.INTERNAL_SERVER_ERROR));
    }

    /**
     * 处理弱密钥异常
     * 当JWT签名使用的密钥强度不够时触发此异常
     *
     * @param e WeakKeyException异常对象
     * @return 表示服务器内部错误的结果对象
     */
    @ExceptionHandler(WeakKeyException.class)
    public Result<String> handleWeakKeyException(WeakKeyException e) {
        log.error("服务器错误：JWT密码太球短咯", e);
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
