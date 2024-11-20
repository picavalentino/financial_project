package com.team.financial_project.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController{
    @GetMapping("mypage")
    public String mypage(){
        return "mypage/mypage";
    }
}
