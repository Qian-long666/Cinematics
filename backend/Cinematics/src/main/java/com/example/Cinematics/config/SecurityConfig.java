package com.example.Cinematics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（前后端分离 + JWT 不需要）
            .csrf(csrf -> csrf.disable())

            // 无状态会话（JWT 不需要服务端 session）
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // CORS 跨域
            .cors(cors -> cors.configurationSource(corsConfigSource()))

            // 权限规则
            .authorizeHttpRequests(auth -> auth
                // ── 登录、注册：公开 ──
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                // ── 读操作：USER 或 ADMIN ──
                .requestMatchers(HttpMethod.GET, "/movie/**", "/movies/**").hasAnyRole("USER", "ADMIN")

                // ── 评论：USER 或 ADMIN ──
                .requestMatchers("/api/comments/**").hasAnyRole("USER", "ADMIN")

                // ── 写操作：仅 ADMIN ──
                .requestMatchers(HttpMethod.POST, "/movie/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/movie/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/movie/**").hasRole("ADMIN")

                // ── 其他所有请求需登录 ──
                .anyRequest().authenticated()
            )

            // 注册 JWT 过滤器（在 Spring Security 的认证过滤器之前执行）
            .addFilterBefore(new JwtAuthFilter(jwtUtil),
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 配置 — 允许前端 localhost:5173 的跨域请求
     */
    @Bean
    public CorsConfigurationSource corsConfigSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
