package com.team.financial_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainContrioller {
    // test용
    @GetMapping("/test")
    public String test() {
        return "/financial/test";
    }

    // test용
    @GetMapping("/main")
    public String main() {
        return "/financial/main";
    }
}
