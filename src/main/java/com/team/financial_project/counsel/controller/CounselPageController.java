package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.service.CounselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/customer/counsel")
public class CounselPageController {

    @Autowired
    private CounselService counselService;

    // 상담 전체 내역 가져오기
    @GetMapping("/getPagedData")
    public Map<String, Object> getPagedData(@RequestParam("page") int page, @RequestParam("size") int size) {
        return counselService.getPagedCounselData(page, size);
    }

    // 특정 고객 상담 내역 가져오기
    @GetMapping("/getPagedDataByCustomer")
    public Map<String, Object> getPagedDataByCustomer(@RequestParam("custId") String custId,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size) {
        return counselService.getPagedCounselDataByCustomerId(custId, page, size);
    }
}
