package com.team.financial_project.management.controller;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.main.service.PaginationService;
import com.team.financial_project.management.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/management")

public class ManagementController {
    @Autowired
    PaginationService paginationService;

    private final ManagementService managementService;

    public  ManagementController(ManagementService managementService){
        this.managementService = managementService;
    }

    @GetMapping("/list")
    public String managementList(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        // 총 페이지 수 계산 (예를 들어, 데이터베이스에 있는 전체 직원 수와 한 페이지당 보여줄 직원 수를 통해 계산)
        int totalDataCount = managementService.getTotalDataCount();
        int pageSize = 10; // 한 페이지에 보여줄 데이터 수
        int totalPageNumber = (int) Math.ceil((double) totalDataCount / pageSize);

        // 페이지네이션 바 번호 계산
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);
        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);

        // 현재 페이지에 맞는 직원 목록 가져오기
        List<UserDTO> managementList = managementService.getManagementList(page, pageSize);
        if (managementList == null || managementList.isEmpty()) {
            model.addAttribute("managementList", Collections.emptyList());
        } else {
            model.addAttribute("managementList", managementList);
        }

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
