package com.team.financial_project.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    // 고객 목록 페이지
    @GetMapping("/list")
    public String customerList() {
        return "customer/customerList"; // 고객 목록 페이지로 이동
    }

    // 고객 메시지 페이지
    @GetMapping("/list/message")
    public String customerMessage() {
        return "customer/customerListModal"; // 고객 메시지 페이지로 이동
    }

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

    // 고객 상세 정보 페이지 (ID 기준 조회)
    @GetMapping("/detail/id")
    public String customerDetailById() {
        return "customer/customerDetail"; // 고객 상세 페이지로 이동
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
