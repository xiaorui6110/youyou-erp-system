package com.xiaorui.youyouerpsystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @description: SpringSecurity 配置
 * @author: xiaorui
 * @date: 2026-03-05 22:04
 **/
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // 关闭 SpringSecurity 自带的的登录认证
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
