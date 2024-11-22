package com.team.financial_project.inquire.controller;

import com.team.financial_project.inquire.service.InquireService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inquire")
public class InquireController {
    private final InquireService inquireService;

    public InquireController(InquireService inquireService) {
        this.inquireService = inquireService;
    }

    //게시글 전체 목록
    @GetMapping("/list")
    public String viewInquireList() {
        return "/inquire/inquire-list";
    }

    //게시글 조건 검색
    @GetMapping("/search")
    public String searchInquires(){
        return "/inquire/inquire-list";
    }
}
