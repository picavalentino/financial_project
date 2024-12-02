package com.team.financial_project.security.etc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 현재 사용자의 인증 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            System.out.println("사용자 : " + auth.getName() + " / 접근 시도한 URL : " + request.getRequestURI());
        }

        // 이전 페이지 URL을 request attribute로 전달
        String referrer = request.getHeader("Referer");
        request.setAttribute("prevPage", referrer);

        // 요청을 커스텀 에러 페이지로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("/system/accessDenied");
        dispatcher.forward(request, response);
    }
}
