package com.team.financial_project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainContrioller {
    // test용
    @GetMapping("/")
    public String test() {
        return "financial/test";
    }
}
