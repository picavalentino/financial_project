package com.team.financial_project.counsel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CounselController {
    @GetMapping("/counsel")
    public String counsel(){
        return "/counsel/counsel";
    }

    @GetMapping("/managementEmployees")
    public String managementEmployees(){
        return "/management/managementEmployees";
    }

    @GetMapping("/managementList")
    public String managementList(){
        return "/management/managementList";
    }
}
