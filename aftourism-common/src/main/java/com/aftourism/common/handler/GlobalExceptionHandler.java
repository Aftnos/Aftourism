package com.aftourism.common.handler;

import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.common.pojo.ResultCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，拦截常见的业务异常与系统异常。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常。
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ResponseResult.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 参数校验异常统一返回。
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ResponseResult<Void> handleValidationException(Exception ex) {
        log.warn("参数校验失败: {}", ex.getMessage());
        return ResponseResult.fail(ResultCode.BAD_REQUEST);
    }

    /**
     * 兜底系统异常处理。
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> handleSystemException(Exception ex) {
        log.error("系统异常", ex);
        return ResponseResult.fail(ResultCode.SYSTEM_ERROR);
    }
}
