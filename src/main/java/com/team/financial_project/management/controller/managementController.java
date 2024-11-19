package com.team.financial_project.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management")
public class managementController {
    @GetMapping("/list")
    public String managementList() {
        return "/management/managementList";
    }

    @GetMapping("/employees")
    public String managementEmployees() {
        return "/management/managementEmployees";
    }
}
