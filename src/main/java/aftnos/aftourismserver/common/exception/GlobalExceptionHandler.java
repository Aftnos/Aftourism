package aftnos.aftourismserver.common.exception;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
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