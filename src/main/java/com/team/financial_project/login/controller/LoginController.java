package com.team.financial_project.login.controller;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.login.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("login")
    public String loginForm(){
        return "/login/login";
    }
    // 회원 등록 맵핑 ==============================================================================
    @GetMapping("register")
    public String registerForm(){
        return "/login/register";
    }

    @GetMapping("register/search")
    @ResponseBody
    public List<UserDTO> searchUsers(@RequestParam("keyword") String keyword) {
        return loginService.findUsersByKeyword(keyword);
    }

    @GetMapping("register/certify")
    @ResponseBody
    public boolean certify_phone(@RequestParam("telno") String telno) {
        log.info("######받은 전화번호" + telno);
        return loginService.certifyByUserTelno(telno);
    }
}
