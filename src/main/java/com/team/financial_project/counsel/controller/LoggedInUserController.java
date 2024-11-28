package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.service.CounselService;
import com.team.financial_project.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class LoggedInUserController {

    private final CounselService counselService;

    public LoggedInUserController(CounselService counselService) {
        this.counselService = counselService;
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserDTO> getLoggedInUserId() {
        // SecurityContextHolder를 통해 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 로그인한 사용자의 username 반환
        String userId = authentication.getName();

        // userId로 UserDTO 조회
        UserDTO user = counselService.getUserById(userId);

        return ResponseEntity.ok(user);
    }
}
