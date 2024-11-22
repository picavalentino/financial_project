package com.team.financial_project.customer.controller;

import com.team.financial_project.customer.service.CustomerService;
import com.team.financial_project.dto.CustomerAndUserCrossDTO;
import com.team.financial_project.dto.CustomerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public String getCustomerList(@RequestParam(name = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                  Model model) {
        // 고객 목록 조회 및 총 고객 수 조회
        List<CustomerAndUserCrossDTO> customers = customerService.getUsersWithPagination(page, pageSize);
        List<CustomerAndUserCrossDTO> users = customerService.getCustomersWithPagination(page, pageSize);

        int totalCustomers = customerService.getTotalCustomerCount();
        int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

        // 모델에 데이터 추가
        model.addAttribute("customers", customers);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "customer/customerList";
    }
    /* ================================================================================================================= */
    // 고객 메시지 페이지
    @GetMapping("/list/message")
    public String customerMessage() {
        return "customer/customerListModal"; // 고객 메시지 페이지로 이동
    }
    /* ================================================================================================================= */
    /* 고객 상세 정보 페이지 */
    @GetMapping("/detail/{custId}")
    public String getCustomerDetail(@PathVariable("custId") String custId, Model model) {
        // 고객 상세 정보 가져오기

        CustomerAndUserCrossDTO customerAndUser = customerService.getCustomerById(custId);
        customerAndUser.getCustomer().splitAddress(); // 주소를 기본 주소와 상세 주소로 나누기
        model.addAttribute("customerAndUser", customerAndUser);
        return "customer/customerDetail"; // 고객 상세 정보 페이지로 이동
    }

    /* ================================================================================================================= */



    // 고객 목록 출력 페이지
    @GetMapping("/list/print")
    public String customerListPrint() {
        return "customer/customerListModal"; // 고객 목록 출력 페이지로 이동
    }

    // 고객 등록 (POST 요청)
    @PostMapping("/list/insert")
    public String customerInsert() {
        return "customer/customerListModal"; // 고객 삽입 페이지로 이동
    }

    // 고객 정보 수정
    @PatchMapping("/detail/id")
    public String customerUpdate() {
        return "customer/customerDetail"; // 고객 수정 페이지로 이동
    }

    // 고객 상세 정보 출력 페이지
    @GetMapping("/detail/id/print")
    public String customerDetailPrint() {
        return "customer/customerDetailPrint"; // 고객 상세 출력 페이지로 이동
    }

    // 담당자 검색 페이지
    @GetMapping("/detail/id/searchManager")
    public String searchManager() {
        return "customer/searchManager"; // 담당자 검색 페이지로 이동
    }
}
