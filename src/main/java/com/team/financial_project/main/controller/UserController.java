package com.team.financial_project.main.controller;

import com.team.financial_project.main.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MainService mainService;

    //로그인
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getUserInfo() {
        String userId = getAuthenticatedUserId(); // 현재 인증된 사용자 ID

        // 사용자 정보 가져오기
        Map<String, String> userInfo = mainService.getUserInfo(userId);

        if (userInfo != null) {
            return ResponseEntity.ok(userInfo); // 사용자 정보 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 사용자 정보 없음
        }
    }
}
