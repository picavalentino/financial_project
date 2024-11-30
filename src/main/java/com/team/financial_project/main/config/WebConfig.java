package com.team.financial_project.main.config;

import com.team.financial_project.main.interceptor.MenuInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MenuInterceptor menuInterceptor;

    public WebConfig(MenuInterceptor menuInterceptor) {
        this.menuInterceptor = menuInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(menuInterceptor)
                .addPathPatterns("/**")  // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/resources/**", "/static/**", "/public/**", "/error")
                .excludePathPatterns("/login", "/signup");  // 로그인 및 회원가입 경로는 제외
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.noCache()); // 캐시 무효화
    }
}
