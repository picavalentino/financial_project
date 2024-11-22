package com.team.financial_project.mypage.controller;

import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.mypage.service.MypageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MypageController{

    private final MypageService mypageService;

    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    @GetMapping("mypage/{userId}")
    public ResponseEntity<MypageDTO> getMypage(@PathVariable String userId) {
        return ResponseEntity.ok(mypageService.getMypageByUserId(userId));
    }
    @GetMapping("/mypage")
    public String mypage(Model model, @RequestParam("userId") String userId) {
        MypageDTO mypageDTO = mypageService.getMypageByUserId(userId);
        model.addAttribute("mypage", mypageDTO);
        return "mypage/mypage"; // Thymeleaf 파일
    }




}
