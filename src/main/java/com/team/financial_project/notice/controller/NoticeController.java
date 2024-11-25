package com.team.financial_project.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system/notice")
public class NoticeController {
    @GetMapping("/list")
    public String viewNoticeList(){
        return "/system/notice-list";
    }

    @GetMapping("/insert")
    public String viewNoticeInsert(){
        return "/system/notice-insert";
    }

}
