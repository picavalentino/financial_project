package com.team.financial_project.system.controller;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.system.service.NoticeService;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("system/notice")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("")
    public String noticeManageView(Model model) {
        List<InquireDTO> list = noticeService.findAllList();
        model.addAttribute("list", list);
        return "system/notice-list";
    }

    @GetMapping("/detail/{inqId}")
    public String noticeDetailView(@PathVariable("inqId") Long inqId, Model model) {
        InquireDTO dto = noticeService.findById(inqId);
        model.addAttribute("dto", dto);
        return "system/notice-detail";
    }
}
