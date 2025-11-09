package com.aftourism.common.aspect;

import com.aftourism.common.logging.OperationLogRecorder;
import com.aftourism.common.model.OperationLogEntry;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspect that records admin controller operations for auditing.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final ObjectProvider<OperationLogRecorder> recorders;

    @Pointcut("execution(* com.aftourism.admin..*Controller.*(..))")
    public void adminControllers() {
        // pointcut definition
    }

    @Around("adminControllers()")
    public Object aroundAdminOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String status = "SUCCESS";
        Object result;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception ex) {
            status = "ERROR: " + ex.getMessage();
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attrs instanceof ServletRequestAttributes
                    ? ((ServletRequestAttributes) attrs).getRequest() : null;
            String path = request != null ? request.getRequestURI() : joinPoint.getSignature().toShortString();
            String method = request != null ? request.getMethod() : "";
            String ip = request != null ? request.getRemoteAddr() : "";
            String params = Arrays.stream(joinPoint.getArgs())
                    .map(arg -> arg == null ? "null" : String.valueOf(arg))
                    .collect(Collectors.joining(", "));
            OperationLogEntry entry = OperationLogEntry.builder()
                    .username(extractUsername())
                    .path(path)
                    .method(method)
                    .params(params)
                    .result(status)
                    .duration(duration)
                    .ip(ip)
                    .createTime(LocalDateTime.now())
                    .build();
            recorders.stream().forEach(recorder -> {
                try {
                    recorder.record(entry);
                } catch (Exception ex) {
                    log.error("Failed to record operation log", ex);
                }
            });
        }
    }

    private String extractUsername() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Object username = request.getAttribute("currentUser");
            if (username != null) {
                return username.toString();
            }
        }
        return "anonymous";
    }
}
