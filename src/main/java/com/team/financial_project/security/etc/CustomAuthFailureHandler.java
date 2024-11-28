package com.team.financial_project.security.etc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 아이디입니다.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "계정이 비활성화되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
        }
        // 로그인 페이지로 리다이렉트하면서 실패 메시지를 전달
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/login?error=true").forward(request, response);
    }
}
