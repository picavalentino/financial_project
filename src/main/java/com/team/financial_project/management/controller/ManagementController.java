package com.team.financial_project.management.controller;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.main.service.PaginationService;
import com.team.financial_project.management.dto.ResponseDTO;
import com.team.financial_project.management.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/management")

public class ManagementController {
    @Autowired
    PaginationService paginationService;

    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
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
            @RequestParam(name = "auth", required = false) String auth,
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

        // 셀렉트 박스에 쓸 권한 정보 가져오기
        List<UserDTO> authList = managementService.getauthList();
        model.addAttribute("authList", authList);

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
        List<UserDTO> employeeList = managementService.getEmployeeList(page, pageSize, dept, position, status, auth, searchField, searchValue);
        model.addAttribute("employeeList", employeeList.isEmpty() ? Collections.emptyList() : employeeList);

        // 페이지 정보 추가
        model.addAttribute("totalPageNumber", totalPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalEmployeeCount", totalEmployeeCount);

        // 검색 조건 모델에 추가
        model.addAttribute("dept", dept);
        model.addAttribute("position", position);
        model.addAttribute("status", status);
        model.addAttribute("auth", auth);
        model.addAttribute("searchField", searchField);
        model.addAttribute("searchValue", searchValue);

        return "management/managementEmployees";
    }

    //    employeeModal 정보 출력
    @GetMapping("/employee/modal")
    @ResponseBody
    public UserDTO managementEmployeeModal(@RequestParam(name = "userId") String userId , Model model) {
        UserDTO users = managementService.findByUserId(userId);

        // 셀렉트 박스에 쓸 부서 정보 가져오기
        List<UserDTO> departmentList = managementService.getDepartmentList();
        model.addAttribute("DepartmentList", departmentList);

        // 셀렉트 박스에 쓸 직위 정보 가져오기
        List<UserDTO> jobPositionList = managementService.getJopPositionList();
        model.addAttribute("JopPositionList", jobPositionList);

        // 셀렉트 박스에 쓸 상태 정보 가져오기
        List<UserDTO> statusList = managementService.getStatusList();
        model.addAttribute("statusList", statusList);

        // 셀렉트 박스에 쓸 권한 정보 가져오기
        List<UserDTO> authList = managementService.getauthList();
        model.addAttribute("authList", authList);

        return users;
    }

//    employeeModal에서 정보 변경
    @PostMapping("/employees/edit")
    public String editEmployee(
        @ModelAttribute UserDTO userDTO,
        RedirectAttributes redirectAttributes) {

        try {
        // 업데이트 서비스 호출
        managementService.updateUser(userDTO);
        redirectAttributes.addFlashAttribute("message", "직원 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "직원 정보 수정 중 오류가 발생했습니다.");
        }

        return "redirect:/management/employees";
    }


    @GetMapping("/insert")
    public String managementInsert(Model model,
                                   @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                   @RequestParam(name = "yearMonth", required = false) String yearMonth) {

        if (yearMonth == null) {
            yearMonth = ""; // 기본값으로 빈 문자열 또는 적절한 값을 설정합니다.
        }

        // 총 데이터 수 계산 (검색 조건 포함)
        int totalInsertCount = managementService.getTotalInsertCount(yearMonth);
        int pageSize = 10;
        int totalPageNumber = (int) Math.ceil((double) totalInsertCount / pageSize);
        System.out.println(totalPageNumber);

        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumber(page, totalPageNumber);
        model.addAttribute("paginationBarNumbers", paginationBarNumbers);
        model.addAttribute("currentPageNumber", page);

        // 현재 페이지에 맞는 직원 목록 가져오기 (검색 조건 포함)
        List<UserDTO> insertList = managementService.getInsertList(page, pageSize, yearMonth);
        model.addAttribute("insertList", insertList.isEmpty() ? Collections.emptyList() : insertList);

        model.addAttribute("totalPageNumber", totalPageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalInsertCount", totalInsertCount);
        model.addAttribute("yearMonth", yearMonth);

        return "/management/managementInsert";
    }

     //직원 등록하는 insertModal
     @GetMapping("/employee/insertModal")
     @ResponseBody
     public ResponseDTO managementInsertModal() {
         // 셀렉트 박스에 사용할 부서 정보 가져오기
         List<UserDTO> departmentList = managementService.getDepartmentList();

         // 셀렉트 박스에 사용할 직위 정보 가져오기
         List<UserDTO> jobPositionList = managementService.getJopPositionList();

         // 셀렉트 박스에 사용할 상태 정보 가져오기
         List<UserDTO> statusList = managementService.getStatusList();

         // 셀렉트 박스에 사용할 권한 정보 가져오기
         List<UserDTO> authList = managementService.getauthList();

         // ResponseDTO 생성 및 반환
         return new ResponseDTO(departmentList, jobPositionList, statusList, authList);
     }

     // 직원등록시 사원번호 생성
     @PostMapping("/generate/userId")
     @ResponseBody
     public String generateEmployeeId(@RequestParam("joiningDate") String joiningDate) {
         // 입사일자 8자리 + 등록 순서 번호로 사원번호 생성 로직
         String datePart = joiningDate.replace("-", ""); // "yyyy-MM-dd" 형식을 "yyyyMMdd"로 변경
         System.out.println(datePart);
         int sequence = managementService.getNextSequenceForDate(joiningDate); // 특정 입사일자에 대해 다음 시퀀스를 가져옴

         String sequencePart = String.format("%03d", sequence); // 시퀀스 번호를 3자리로 만들기
         return datePart + sequencePart;
     }

    // insertModal로 직원 등록
     @PostMapping("/insert/detail")
    public String insertEmployee(UserDTO userDTO){
        managementService.insertEmployee(userDTO);
        return "redirect:/management/insert";
     }
}
