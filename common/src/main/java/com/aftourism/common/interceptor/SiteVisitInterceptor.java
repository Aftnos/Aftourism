package com.aftourism.common.interceptor;

import com.aftourism.common.metrics.SiteVisitCounter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor increasing visit counter for each request.
 */
@Component
@RequiredArgsConstructor
public class SiteVisitInterceptor implements HandlerInterceptor {

    private final SiteVisitCounter siteVisitCounter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        siteVisitCounter.increment();
        return true;
    }
}
