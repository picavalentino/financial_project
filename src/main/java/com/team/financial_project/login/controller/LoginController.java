package com.team.financial_project.login.controller;

import com.team.financial_project.login.service.LoginService;
import com.team.financial_project.main.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @GetMapping("exist")
    @ResponseBody
    public boolean hasExist(@RequestParam("name") String userName, @RequestParam("telno") String userTelno){
        return loginService.hasExist(userName, userTelno);
    }

    // 인증 번호 전송
    @GetMapping("send-auth-code")
    @ResponseBody
    public boolean sendAuthCode(@RequestParam("telno") String userTelno){
        return smsService.sendVerificationCode(userTelno);
    }

    // 인증 번호 확인
    @GetMapping("check-auth-code")
    @ResponseBody
    public boolean checkAuthCode(@RequestParam("telno") String userTelno, @RequestParam("code") String code){
        return smsService.verifyCode(userTelno, code);
    }

    // 사원번호 보내기
    @GetMapping("retrieve-id")
    @ResponseBody
    public String findUserIdByUserTelno(@RequestParam("telno") String userTelno){
        return loginService.findUserId(userTelno);
    }

    // 비밀번호 찾기 ===============================================================================

}
