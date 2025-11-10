package aftnos.aftourismserver.common.exception;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException e) {
        log.error("参数验证失败: {}", e.getMessage());
        return Result.error(ResultCode.DATA_INCORRECT);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("请求没参数不合法: {}", e.getMessage());
        return Result.error(ResultCode.DATA_INCOMPLETE);
    }
    
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(ResultCode.BUSINESS_EXCEPTION,e.getMessage());
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Result<String>> handleUnauthorizedException(UnauthorizedException e) {
        log.warn("鉴权失败: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Result.error(ResultCode.NOT_LOGIN));
    }
    
    @ExceptionHandler
    public Result<String> error(Exception e){
        log.error("错误",e);
        return Result.error(ResultCode.INTERNAL_ERROR);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<String> DuplicateKeyException(DuplicateKeyException e){
        log.error("错误",e);
        String message = e.getMessage();

        // 查找引号中的内容
        int start = message.indexOf("'");
        int end = message.indexOf("'", start + 1);
        String msg = message.substring(start + 1, end);
        return Result.error("QAQ~，" + msg + " 已存在");
    }

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
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        String message = e.getMessage();

        // 检查是否是部门删除异常
        if (message != null && message.contains("该部门下存在员工，无法删除")) {
            return Result.error(ResultCode.DATA_USED);
        }

        return Result.error(ResultCode.INTERNAL_ERROR);
    }
}