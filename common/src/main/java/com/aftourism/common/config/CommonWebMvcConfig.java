package com.aftourism.common.config;

import com.aftourism.common.interceptor.SiteVisitInterceptor;
import com.aftourism.common.storage.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registers shared MVC infrastructure components.
 */
@Configuration
@RequiredArgsConstructor
public class CommonWebMvcConfig implements WebMvcConfigurer {

    private final SiteVisitInterceptor siteVisitInterceptor;
    private final FileStorageProperties fileStorageProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(siteVisitInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = fileStorageProperties.getUploadDir();
        if (uploadDir != null && !uploadDir.isBlank()) {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:" + uploadDir + (uploadDir.endsWith("/") ? "" : "/"));
        }
    }
}
