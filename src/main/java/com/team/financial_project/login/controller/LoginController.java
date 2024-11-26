package com.team.financial_project.login.controller;

import com.team.financial_project.login.service.LoginService;
import com.team.financial_project.main.service.SmsService;
import com.team.financial_project.main.util.SmsVerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("login")
public class LoginController {
    private final LoginService loginService;
    private final SmsService smsService;

    public LoginController(LoginService loginService, SmsService smsService) {
        this.loginService = loginService;
        this.smsService = smsService;
    }

    @GetMapping("")
    public String loginForm(){
        return "login/login";
    }
    // 아이디 찾기 ================================================================================
    @GetMapping("retrieve-id")
    @ResponseBody
    public String sendAuthCode(@RequestParam("user_name") String userName,
                                                @RequestParam("user_telno") String userTelno){
        String verificationCode = SmsVerificationUtil.generateVerificationCode();
        String message = "Your verification code is: " + verificationCode;

        // 이곳에서 데이터베이스에 인증 코드를 저장하는 로직을 추가할 수 있습니다.

        return smsService.sendVerificationCode(userTelno);
    }

    // 비밀번호 찾기 ===============================================================================


}
