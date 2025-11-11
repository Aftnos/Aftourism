package aftnos.aftourismserver.common.interceptor;

import aftnos.aftourismserver.monitor.service.SiteVisitStatsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 访问统计拦截器，负责记录站点 PV/UV/IP 数据
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SiteVisitStatsInterceptor implements HandlerInterceptor {

    private final SiteVisitStatsService siteVisitStatsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String clientIp = extractClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        siteVisitStatsService.recordVisit(clientIp, userAgent);
        return true;
    }

    /**
     * 解析客户端真实 IP，优先读取代理头
     */
    private String extractClientIp(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };
        for (String headerName : headerNames) {
            String headerValue = request.getHeader(headerName);
            if (headerValue != null && !headerValue.isBlank() && !"unknown".equalsIgnoreCase(headerValue)) {
                // X-Forwarded-For 可能包含多个 IP，取第一个即可
                return headerValue.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}
