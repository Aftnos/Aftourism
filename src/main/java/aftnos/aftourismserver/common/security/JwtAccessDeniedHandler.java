package aftnos.aftourismserver.common.security;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.result.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT权限不足处理器
 * 当用户尝试访问没有权限的资源时，该类会处理此类异常并返回统一格式的错误响应
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * JSON序列化工具，用于将Java对象转换为JSON字符串
     */
    private final ObjectMapper objectMapper;

    /**
     * 构造函数注入ObjectMapper实例
     * @param objectMapper JSON序列化工具
     */
    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 处理权限不足异常的方法
     * 当用户已认证但权限不足时，Spring Security会调用此方法
     *
     * @param request HttpServletRequest对象，包含客户端请求信息
     * @param response HttpServletResponse对象，用于向客户端发送响应
     * @param accessDeniedException 权限不足异常对象，包含异常详情
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // 设置HTTP状态码为403 Forbidden，表示服务器理解请求但拒绝执行
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 设置响应内容类型为JSON格式
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 创建一个包含权限拒绝错误码的统一结果对象
        Result<String> result = Result.error(ResultCode.PERMISSION_DENIED);
        // 将结果对象序列化为JSON字符串并写入响应体中返回给客户端
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}