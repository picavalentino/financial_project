package com.team.financial_project.security.config;

import com.team.financial_project.security.etc.CustomAuthFailureHandler;
import com.team.financial_project.security.service.CustomUserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{
    private final CustomUserDetailService customUserDetailService;

    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((auth)-> auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/login/**", "/register/**","/system/**","/management/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated() // 그외 모든 요청은 인증된 사용자들만 접근 가능
        );
        http.formLogin((auth)-> auth.loginPage("/login").loginProcessingUrl("/loginProc")
                .usernameParameter("user_id").passwordParameter("user_pw")
                .defaultSuccessUrl("/main", true)
                .failureHandler(new CustomAuthFailureHandler())
                .permitAll());
        http.logout((auth)->auth
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write("{\"message\": \"로그아웃이 성공적으로 처리되었습니다.\"}");
                    response.getWriter().flush();
                })
                .invalidateHttpSession(true)
                .permitAll());
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
