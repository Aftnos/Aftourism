package aftnos.aftourismserver.common.config;

import aftnos.aftourismserver.common.interceptor.SiteVisitStatsInterceptor;
import aftnos.aftourismserver.file.config.FileStorageProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.util.StringUtils;

/**
 * Web MVC 配置，主要用于注册登录拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final SiteVisitStatsInterceptor siteVisitStatsInterceptor;
    private final FileStorageProperties fileStorageProperties;

    public WebMvcConfig(SiteVisitStatsInterceptor siteVisitStatsInterceptor,
                        FileStorageProperties fileStorageProperties) {
        this.siteVisitStatsInterceptor = siteVisitStatsInterceptor;
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(siteVisitStatsInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 静态资源映射，将上传目录映射为可访问的 HTTP 路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (fileStorageProperties == null || !StringUtils.hasText(fileStorageProperties.getUploadDir())) {
            return;
        }
        String accessPattern = fileStorageProperties.getAccessPattern();
        String uploadDir = fileStorageProperties.getUploadDir();
        if (!accessPattern.endsWith("**")) {
            accessPattern = accessPattern.endsWith("/") ? accessPattern + "**" : accessPattern + "/**";
        }
        String location = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";
        registry.addResourceHandler(accessPattern)
                .addResourceLocations("file:" + location);
    }
}
