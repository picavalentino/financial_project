package com.team.financial_project.login.controller;

import com.team.financial_project.login.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("login")
    public String loginForm(){
        return "/login/login";
    }

    @GetMapping("register")
    public String registerForm(){
        return "/login/register";
    }
}
