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

/**
 * Spring Security 核心配置，启用基于 JWT 的无状态认证。
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                        .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                        "/portal/auth/**",  //门户登录
                                        "/admin/auth/**",   //管理员登录
                                        "/error",           //错误页面
                                        "/files/**"         //文件下载
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/portal/scenic/**",    //门户景区信息查询
                                        "/portal/venue/**",     //门户场馆信息查询
                                        "/portal/activity/**",  //门信活动息查询
                                        "/portal/notice/**"     //门户通知信息查询
                                ).permitAll()
                                .requestMatchers(
                                        "/portal/fav/**",               //门户收藏
                                        "/portal/activity/apply",       //门户活动报名
                                        "/portal/activity/*/comment"    //门户活动留言
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