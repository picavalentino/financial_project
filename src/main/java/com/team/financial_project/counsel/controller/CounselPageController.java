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

    @GetMapping("/getPagedData")
    public Map<String, Object> getPagedData(@RequestParam("page") int page, @RequestParam("size") int size) {
        return counselService.getPagedCounselData(page, size);
    }
}
