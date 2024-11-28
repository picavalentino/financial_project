package com.team.financial_project.customer.controller;

import com.team.financial_project.customer.service.CustomerService;
import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.inquire.service.InquireService;
import com.team.financial_project.management.service.ManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ManagementService managementService;
    private final InquireService inquireService;

    public CustomerController(CustomerService customerService, ManagementService managementService, InquireService inquireService) {
        this.customerService = customerService;
        this.managementService = managementService;
        this.inquireService = inquireService;
    }

    //로그인
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
    }

    @GetMapping("/list")
    public String getCustomerList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "searchType", required = false) String searchType,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        // 로그인된 사용자 ID 설정
        String userId = getAuthenticatedUserId();
        String userName = inquireService.getUserName(userId);
        model.addAttribute("userName", userName);
        model.addAttribute("staffId", userId);

        // 검색 조건 기본값 설정
        if (searchType == null && keyword == null) {
            searchType = "manager";
            keyword = userName; // 로그인된 사용자 이름을 기본값으로 설정
        }

        // 검색 조건과 키워드에 따라 페이징 처리된 고객 목록 조회
        List<CustomerDTO> customers = !searchType.isEmpty() && !keyword.isEmpty()
                ? customerService.searchCustomersWithPagination(page, pageSize, searchType, keyword)
                : customerService.getCustomersWithPagination(page, pageSize);

        // 검색 조건에 따른 총 고객 수 계산
        int totalCustomers = !searchType.isEmpty() && !keyword.isEmpty()
                ? customerService.getTotalCustomerCount(searchType, keyword)
                : customerService.getTotalCustomerCount(null, null);
        int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

        // 모델에 데이터 추가
        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "customer/customerList"; // 뷰 이름
    }

    /* ================================================================================================================= */

    // 고객 등록 (POST 요청)
    @PostMapping("/list/insert")
    @ResponseBody
    public ResponseEntity<?> insertCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            // 고객 등록 로직 수행
            customerService.insertCustomer(customerDTO);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "고객이 성공적으로 등록되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "고객 등록에 실패했습니다: " + e.getMessage()));
        }
    }

    /* ================================================================================================================= */
    /* 고객 상세 정보 페이지 */
    @GetMapping("/detail/{custId}")
    public String getCustomerDetail(@PathVariable("custId") String custId, Model model) {

        // 로그인된 사용자 ID 설정
        String staffId = getAuthenticatedUserId();
        model.addAttribute("staffId",staffId);

        // 고객 상세 정보 가져오기
        CustomerDTO customer = customerService.getCustomerById(custId);

        // 고객 직업정보 셀렉트 박스
        List<CustomerDTO> custOccpTyCdList = customerService.getCustOccpTyCdList();

        model.addAttribute("custOccpTyCdList",custOccpTyCdList);
        model.addAttribute("customer", customer);
        return "customer/customerDetail"; // 고객 상세 정보 페이지로 이동
    }

    // 담당자 검색 API
    @GetMapping("/detail/{custId}/searchManager")
    public ResponseEntity<List<UserDTO>> searchManagers(@PathVariable("custId") String custId, @RequestParam("name") String name) {
        List<UserDTO> managers = managementService.getManagersByName(name);
        return ResponseEntity.ok(managers);
    }

    /* 수정하기 */
    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            customerService.updateCustomer(customerDTO);
            return ResponseEntity.ok("고객 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();  // 예외의 전체 스택 트레이스를 로그에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("고객 정보 수정에 실패했습니다. 오류 메시지: " + e.getMessage());
        }
    }
    /* ================================================================================================================= */
}
