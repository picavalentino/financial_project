package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.dto.CodeDTO;
import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.counsel.service.CounselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String counsel(Model model){
        // 코드 리스트 조회 후 모델에 추가
        List<CodeDTO> counselCategories = counselService.getCodeListByCl("700");
        model.addAttribute("counselCategories", counselCategories);
        return "counsel/counsel";
    }
}
