package aftnos.aftourismserver.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * HTTP请求日志记录配置类
 * 用于在控制台输出所有HTTP请求信息，便于前后端联调
 */
@Configuration
public class RequestLoggingConfig {

    /**
     * 配置请求日志过滤器
     * 输出所有HTTP请求的详细信息到控制台
     * 
     * @return CommonsRequestLoggingFilter 过滤器实例
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        // 启用请求日志记录
        loggingFilter.setIncludePayload(true);
        // 设置最大payload长度
        loggingFilter.setMaxPayloadLength(1024);
        // 包含查询字符串
        loggingFilter.setIncludeQueryString(true);
        // 包含请求头
        loggingFilter.setIncludeHeaders(true);
        // 包含客户端地址
        loggingFilter.setIncludeClientInfo(true);
        // 确保在DispatcherServlet之前记录日志
        loggingFilter.setBeforeMessagePrefix("[HTTP REQUEST] ");
        loggingFilter.setAfterMessagePrefix("[HTTP RESPONSE] ");
        return loggingFilter;
    }
    
    /**
     * 注册请求日志过滤器，设置最高优先级以确保能捕获所有请求，包括401响应
     * 
     * @param filter CommonsRequestLoggingFilter实例
     * @return FilterRegistrationBean<CommonsRequestLoggingFilter>
     */
    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> loggingFilterRegistration(CommonsRequestLoggingFilter filter) {
        FilterRegistrationBean<CommonsRequestLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setName("requestLoggingFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // 设置为最高优先级
        registration.addUrlPatterns("/*"); // 捕获所有URL模式
        return registration;
    }
}