package com.team.financial_project.management.controller;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.main.service.PaginationService;
import com.team.financial_project.management.service.ManagementService;
import org.apache.catalina.User;
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
    public String managementList(
            Model model,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "dept", required = false) String dept,
            @RequestParam(name = "position", required = false) String position,
            @RequestParam(name = "searchField", required = false) String searchField,
            @RequestParam(name = "searchValue", required = false) String searchValue) {

        // 셀렉트 박스에 쓸 부서 정보 가져오기
        List<UserDTO> departmentList = managementService.getDepartmentList();
        model.addAttribute("DepartmentList", departmentList);

        // 셀렉트 박스에 쓸 직위 정보 가져오기
        List<UserDTO> jobPositionList = managementService.getJopPositionList();
        model.addAttribute("JopPositionList", jobPositionList);

        // 총 데이터 수 계산 (검색 조건 포함)
        int totalDataCount = managementService.getTotalDataCount(dept, position, searchField, searchValue);
        int pageSize = 10; // 한 페이지에 보여줄 데이터 수
        int totalPageNumber = (int) Math.ceil((double) totalDataCount / pageSize);

        // 페이지네이션 바 번호 계산
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);
        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);

        // 현재 페이지에 맞는 직원 목록 가져오기 (검색 조건 포함)
        List<UserDTO> managementList = managementService.getManagementList(page, pageSize, dept, position, searchField, searchValue);
        model.addAttribute("managementList", managementList.isEmpty() ? Collections.emptyList() : managementList);

        // 페이지 정보 추가
        model.addAttribute("totalPageNumber", totalPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalDataCount", totalDataCount);

        // 검색 조건 모델에 추가
        model.addAttribute("dept", dept);
        model.addAttribute("position", position);
        model.addAttribute("searchField", searchField);
        model.addAttribute("searchValue", searchValue);

        return "/management/managementList";
    }


    @GetMapping("/employees")
    public String managementEmployees(
            Model model,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "dept", required = false) String dept,
            @RequestParam(name = "position", required = false) String position,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "searchField", required = false) String searchField,
            @RequestParam(name = "searchValue", required = false) String searchValue) {

        // 셀렉트 박스에 쓸 부서 정보 가져오기
        List<UserDTO> departmentList = managementService.getDepartmentList();
        model.addAttribute("DepartmentList", departmentList);

        // 셀렉트 박스에 쓸 직위 정보 가져오기
        List<UserDTO> jobPositionList = managementService.getJopPositionList();
        model.addAttribute("JopPositionList", jobPositionList);

        // 셀렉트 박스에 쓸 상태 정보 가져오기
        List<UserDTO> statusList = managementService.getStatusList();
        model.addAttribute("statusList", statusList);

        // 총 데이터 수 계산 (검색 조건 포함)
        int totalEmployeeCount = managementService.getTotalEmployeeCount(dept, position, status, searchField, searchValue);
        int pageSize = 10; // 한 페이지에 보여줄 데이터 수
        int totalPageNumber = (int) Math.ceil((double) totalEmployeeCount / pageSize);
        System.out.println(totalPageNumber);

        // 페이지네이션 바 번호 계산
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);
        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);

        // 현재 페이지에 맞는 직원 목록 가져오기 (검색 조건 포함)
        List<UserDTO> employeeList = managementService.getEmployeeList(page, pageSize, dept, position, status, searchField, searchValue);
        model.addAttribute("employeeList", employeeList.isEmpty() ? Collections.emptyList() : employeeList);

        // 페이지 정보 추가
        model.addAttribute("totalPageNumber", totalPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalEmployeeCount", totalEmployeeCount);

        // 검색 조건 모델에 추가
        model.addAttribute("dept", dept);
        model.addAttribute("position", position);
        model.addAttribute("status", status);
        model.addAttribute("searchField", searchField);
        model.addAttribute("searchValue", searchValue);

        return "management/managementEmployees";
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
