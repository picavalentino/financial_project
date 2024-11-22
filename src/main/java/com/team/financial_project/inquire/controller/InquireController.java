package com.team.financial_project.inquire.controller;

import com.team.financial_project.inquire.service.InquireService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    //게시글 상세보기
    @GetMapping("/detail/{inqId}")
    public String viewInquireDetail() {
        return "/inquire/inquire-detail";
    }

    //신규 게시글 등록
    @GetMapping("/insert")
    public String insertInquireView(){
        return "/inquire/inquire-insert";
    }

    @PostMapping("/insert")
    public String insertInquireProc(){
        return "redirect:/inquire/list";
    }

    //게시글 수정
    @PostMapping("/detail/{inqId}/update")
    public String inquireUpdate() {
        String url = "redirect:/inquire/detail/";
        return url;
    }

    //게시글 삭제
    @PostMapping("/detail/{inqId}/delete")
    public String inquireDelete(){
        return "redirect:/inquire/list";
    }
}
