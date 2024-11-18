package com.team.financial_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((auth)-> auth.requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
        );
        http.formLogin((auth)-> auth.loginPage("/login").loginProcessingUrl("/loginProc").usernameParameter("email").defaultSuccessUrl("/").permitAll());
        http.logout((auth)-> auth.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());
        http.sessionManagement((auth)-> auth.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).invalidSessionUrl("/login")
                .sessionFixation().none()
                .maximumSessions(1).expiredUrl("/login"));
        // .sessionCreationPolicy()로그인과 같은 처리로 세션이 활성화되지 않으면 타임아웃이 발생 안함
        // .invalidSessionUrl() 유효하지 않은 세션 처리
        // .sessionFixation().none() 세션이 고정되지 않도록
        http.csrf((auth) -> auth.disable());
        http.oauth2Login((oAuth)-> oAuth.loginPage("/login").defaultSuccessUrl("/").userInfoEndpoint((userInfo)-> userInfo.userService(oauth2UserService)));
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
