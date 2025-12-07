package aftnos.aftourismserver.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 核心配置，启用基于 JWT 的无状态认证 + CORS。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * 全局 CORS 配置，供 Spring Security 使用。
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // 本地开发前端地址，按需增减
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://localhost:*"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "X-Trace-Id"));
        cfg.setAllowCredentials(true); // 前端带 cookie / Authorization 时需要
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用上述 CORS 规则
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ 不要 disable CORS，这里启用并使用上面的配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        // ⚠ 预检请求必须放行，否则浏览器根本发不到真正的 GET/POST
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/portal/auth/**",  // 门户登录
                                "/auth/**",  // 通用登录
                                "/admin/auth/**",   // 管理员登录
                                "/error",           // 错误页面
                                "/files/**",        // 文件下载
                                "/ping"             // 健康检查（如果有）
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/portal/scenic/**",    // 门户景区信息查询
                                "/portal/venue/**",     // 门户场馆信息查询
                                "/portal/activity/**",  // 门户活动信息查询
                                "/portal/notice/**"     // 门户通知信息查询
                        ).permitAll()
                        .requestMatchers(
                                "/portal/fav/**",               // 门户收藏
                                "/portal/activity/apply",       // 门户活动报名
                                "/portal/activity/*/comment"    // 门户活动留言
                        ).hasRole("PORTAL_USER")
                        .requestMatchers("/admin/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 添加空的UserDetailsService以避免Spring Security自动配置警告
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> { throw new UnsupportedOperationException("此应用使用JWT认证，不使用UserDetailsService"); };
    }
}
