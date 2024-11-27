package com.team.financial_project.login.controller;

import com.team.financial_project.login.service.LoginService;
import com.team.financial_project.main.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
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

    // 문자 인증 ================================================================================
    // 인증 번호 전송
    @GetMapping("send-auth-code")
    @ResponseBody
    public String sendAuthCode(@RequestParam("telno") String userTelno){
        return smsService.sendVerificationCode(userTelno);
    }

    // 인증 번호 확인
    @GetMapping("check-auth-code")
    @ResponseBody
    public boolean checkAuthCode(@RequestParam("telno") String userTelno, @RequestParam("code") String code){
        return smsService.verifyCode(userTelno, code);
    }

    // 아이디 찾기 ================================================================================

    @GetMapping("exist/id")
    @ResponseBody
    public boolean hasExistByName(@RequestParam("name") String userName,
                            @RequestParam("telno") String userTelno){
        return loginService.hasExistByUser_name(userName, userTelno);
    }

    // 사원번호 보내기
    @GetMapping("retrieve-id")
    @ResponseBody
    public String findUserIdByUserTelno(@RequestParam("telno") String userTelno){
        return loginService.findUserId(userTelno);
    }

    // 비밀번호 찾기 ===============================================================================

    @GetMapping("exist/pw")
    @ResponseBody
    public boolean hasExist(@RequestParam("id") String userId,
                            @RequestParam("telno") String userTelno){
        return loginService.hasExistByUser_id(userId, userTelno);
    }

    @GetMapping("reset-pw")
    @ResponseBody
    public String resetPw(@RequestParam("id") String userId){
        if(loginService.resetUserPw(userId)){
            return "a123456789";
        }else {
            return "";
        }
    }

    @GetMapping("change-pw")
    @ResponseBody
    public boolean changePw(@RequestParam("id") String userId, @RequestParam("pw") String userPw){
        return loginService.changeUserPw(userId, userPw);
    }
}
