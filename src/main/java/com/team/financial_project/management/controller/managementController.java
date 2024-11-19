package com.team.financial_project.management.controller;

import com.team.financial_project.main.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/management")

public class managementController {
    @Autowired
    PaginationService paginationService;


    @GetMapping("/list")
    public String managementList(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        int totalPageNumber = 10; // 실제 데이터에 맞춰 계산된 총 페이지 수
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);

        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);
        return "management/managementList";
    }

    @GetMapping("/employees")
    public String managementEmployees(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        int totalPageNumber = 10; // 실제 데이터에 맞춰 계산된 총 페이지 수
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);

        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);
        return "/management/managementEmployees";
    }

    @GetMapping("/insert")
    public String managementInsert(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        int totalPageNumber = 10; // 실제 데이터에 맞춰 계산된 총 페이지 수
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);

        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);
        return "/management/managementInsert";
    }
}
