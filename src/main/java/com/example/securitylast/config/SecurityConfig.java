package com.example.securitylast.config;

import com.example.securitylast.filter.LoginFilter;
import com.example.securitylast.handler.*;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//开启SpringSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorization -> {
                    authorization.requestMatchers("/user/**", "/login", "logout").permitAll()
                            .anyRequest().authenticated();
                })
                // 基于token 不需要CSRF
                .csrf().disable()
                // 开启跨域以便前端调用
                .cors()
                .and()
                    .formLogin()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(new MyAccessDeniedHandler())
                    .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and()
                    .addFilterAt(loginFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public Filter loginFilter(AuthenticationManager authenticationManager) {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/login");
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        loginFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        return loginFilter;
    }

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
