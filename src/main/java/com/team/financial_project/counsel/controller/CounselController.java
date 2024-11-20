package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.counsel.service.CounselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/customer")
public class CounselController {

    @Autowired
    private CounselService counselService;

    // MyBatis Test
    @GetMapping("/mybatisTest")
    @ResponseBody
    public String testConnection() {
        int result = counselService.checkConnection();
        return result == 1 ? "DB 연결 성공!" : "DB 연결 실패!";
    }

    @GetMapping("/counsel")
    public String counsel(){
        return "/counsel/counsel";
    }

//    @PostMapping("/counsel/insertCounsel")
//    public String insertCounsel(TbCounselDTO dto){
//        counselService.insertCounsel(dto);
//        return "redirect:/customer/counsel";
//    }

    @GetMapping("/managementEmployees")
    public String managementEmployees(){
        return "/management/managementEmployees";
    }

    @GetMapping("/managementList")
    public String managementList(){
        return "/management/managementList";
    }
}
