package com.aftourism.common.context;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Simple servlet filter copying username header into both request attribute and thread local context.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserContextFilter implements Filter {

    private static final String HEADER_NAME = "X-USER";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            if (request instanceof HttpServletRequest httpServletRequest) {
                String username = httpServletRequest.getHeader(HEADER_NAME);
                if (username != null && !username.isBlank()) {
                    httpServletRequest.setAttribute("currentUser", username);
                    UserContext.setUsername(username);
                }
            }
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
