package com.team.financial_project.authsys.controller;

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
@RequestMapping("/system")
public class AuthSystemController {

    @Autowired
    private PaginationService paginationService;

    private final ManagementService managementService;

    public AuthSystemController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping("/auth")
    public String authList(
            @RequestParam(defaultValue = "1") int page, // 페이지 번호, 기본값은 1
            @RequestParam(required = false) String auth, // 권한 검색 조건
            Model model) {

        // 셀렉트 박스에 쓸 권한 정보 가져오기
        List<UserDTO> authList = managementService.getauthList(); // 권한 목록을 가져오는 메소드
        model.addAttribute("authList", authList);

        // 총 데이터 수 계산 (검색 조건 포함)
        int totalAuthCount = managementService.getTotalAuthCount(auth);
        int pageSize = 10; // 한 페이지에 보여줄 데이터 수
        int totalPageNumber = (int) Math.ceil((double) totalAuthCount / pageSize); // 전체 페이지 수 계산

        // page 값 유효성 검사
        if (page < 1) {
            page = 1; // 최소값 보정
        } else if (page > totalPageNumber) {
            page = totalPageNumber; // 최대값 보정
        }

        // 페이지네이션 바 번호 계산
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);
        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);

        // 현재 페이지에 맞는 데이터 목록 가져오기 (검색 조건 포함)
        List<UserDTO> managementList = managementService.getauthList(page, pageSize, auth);
        model.addAttribute("managementList", managementList.isEmpty() ? Collections.emptyList() : managementList);

        // 페이지 정보 모델에 추가
        model.addAttribute("totalPageNumber", totalPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalDataCount", totalAuthCount);

        // 검색 조건 모델에 추가
        model.addAttribute("auth", auth);

        return "/authsys/authSystem";
    }
}
