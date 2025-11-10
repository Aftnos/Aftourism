package aftnos.aftourismserver.common.aop;

import aftnos.aftourismserver.admin.mapper.OperationLogMapper;
import aftnos.aftourismserver.admin.pojo.OperationLog;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.interceptor.JwtAuthenticationInterceptor;
import aftnos.aftourismserver.common.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogMapper operationLogMapper;
    private final JwtUtils jwtUtils;

    public OperationLogAspect(OperationLogMapper operationLogMapper, JwtUtils jwtUtils) {
        this.operationLogMapper = operationLogMapper;
        this.jwtUtils = jwtUtils;
    }

    @Around("execution(* aftnos.aftourismserver.*.controller..*.*(..))")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 创建操作日志对象
        OperationLog operationLog = new OperationLog();

        try {
            // 获取操作人信息
            OperatorInfo operatorInfo = getOperatorInfo();
            operationLog.setOperatorId(operatorInfo.getOperatorId());
            operationLog.setOperatorType(operatorInfo.getOperatorType());

            // 获取请求相关信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                operationLog.setRequestUri(request.getRequestURI());
                operationLog.setRequestMethod(request.getMethod());
                operationLog.setIpAddress(request.getRemoteAddr());
                operationLog.setUserAgent(request.getHeader("User-Agent"));
                
                // 从URI中提取模块名称
                String requestUri = request.getRequestURI();
                if (requestUri != null && requestUri.length() > 1) {
                    String[] uriParts = requestUri.split("/");
                    if (uriParts.length > 1) {
                        operationLog.setModuleName(uriParts[1]);
                    }
                }
            }

            // 获取目标类名和方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operationLog.setClassMethod(className + "." + methodName);
            
            // 设置操作名称
            operationLog.setOperationName(getOperationName(methodName));

            // 获取方法参数
            Object[] args = joinPoint.getArgs();
            String methodParams = Arrays.toString(args);
            // 限制参数长度，避免超出数据库字段限制
            if (methodParams.length() > 2000) {
                methodParams = methodParams.substring(0, 2000);
            }
            operationLog.setRequestParams(methodParams);

            // 执行目标方法
            Object result = joinPoint.proceed();

            // 记录返回值
            String returnValue = result != null ? result.toString() : "void";
            // 限制返回值长度，避免超出数据库字段限制
            if (returnValue.length() > 2000) {
                returnValue = returnValue.substring(0, 2000);
            }
            operationLog.setResponseBody(returnValue);

            // 计算执行时间
            long costTime = System.currentTimeMillis() - startTime;
            operationLog.setCostMs((int) costTime);
            
            // 设置成功标志
            operationLog.setSuccessFlag(true);

            // 保存日志到数据库
            operationLogMapper.insert(operationLog);

            return result;
        } catch (Throwable throwable) {
            // 记录执行时间（即使出错也要记录）
            long costTime = System.currentTimeMillis() - startTime;
            operationLog.setCostMs((int) costTime);
            
            // 设置失败标志和错误信息
            operationLog.setSuccessFlag(false);
            operationLog.setErrorMsg(throwable.getMessage());
            
            // 限制错误信息长度
            if (operationLog.getErrorMsg() != null && operationLog.getErrorMsg().length() > 500) {
                operationLog.setErrorMsg(operationLog.getErrorMsg().substring(0, 500));
            }

            // 即使出错也保存日志
            try {
                operationLogMapper.insert(operationLog);
            } catch (Exception e) {
                // 记录保存日志出错的情况，但不影响原方法执行
                log.error("保存操作日志失败: ", e);
            }

            // 抛出原始异常
            throw throwable;
        }
    }

    /**
     * 根据方法名获取操作名称
     * @param methodName 方法名
     * @return 操作名称
     */
    private String getOperationName(String methodName) {
        if (methodName.startsWith("add") || methodName.startsWith("create")) {
            return "新增";
        } else if (methodName.startsWith("update") || methodName.startsWith("modify")) {
            return "修改";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "删除";
        } else if (methodName.startsWith("get") || methodName.startsWith("find") || methodName.startsWith("query")) {
            return "查询";
        } else if (methodName.startsWith("login")) {
            return "登录";
        } else if (methodName.startsWith("logout")) {
            return "登出";
        } else {
            return methodName;
        }
    }

    /**
     * 从请求头中获取操作人信息
     * @return 操作人信息
     */
    private OperatorInfo getOperatorInfo() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 优先从拦截器写入的请求属性中获取用户信息，避免重复解析
                Object cachedUser = request.getAttribute(JwtAuthenticationInterceptor.ATTR_USER_INFO);
                if (cachedUser instanceof User user) {
                    OperatorInfo operatorInfo = new OperatorInfo();
                    operatorInfo.setOperatorId(user.getId());
                    operatorInfo.setOperatorType("USER");
                    return operatorInfo;
                }

                Object cachedUserId = request.getAttribute(JwtAuthenticationInterceptor.ATTR_USER_ID);
                if (cachedUserId instanceof Long userIdAttr) {
                    OperatorInfo operatorInfo = new OperatorInfo();
                    operatorInfo.setOperatorId(userIdAttr);
                    operatorInfo.setOperatorType("USER");
                    return operatorInfo;
                }

                String token = resolveToken(request);
                if (StringUtils.hasText(token)) {
                    Long userId = jwtUtils.parseUserId(token);
                    OperatorInfo operatorInfo = new OperatorInfo();
                    operatorInfo.setOperatorId(userId);
                    operatorInfo.setOperatorType("USER");
                    return operatorInfo;
                }
            }
        } catch (Exception e) {
            log.error("获取操作人信息失败: ", e);
        }
        return new OperatorInfo(); // 返回默认值
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return request.getHeader("token");
    }
    
    /**
     * 操作人信息内部类
     */
    private static class OperatorInfo {
        private Long operatorId;
        private String operatorType;
        
        public Long getOperatorId() {
            return operatorId;
        }
        
        public void setOperatorId(Long operatorId) {
            this.operatorId = operatorId;
        }
        
        public String getOperatorType() {
            return operatorType;
        }
        
        public void setOperatorType(String operatorType) {
            this.operatorType = operatorType;
        }
    }
}