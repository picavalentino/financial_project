package com.team.financial_project.mypage.controller;

import com.team.financial_project.login.dto.UserDTO;
import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.mypage.mapper.MypageMapper;
import com.team.financial_project.mypage.service.MypageService;
import com.team.financial_project.mypage.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class MypageController{

    private final MypageService mypageService;
    private final MypageMapper mypageMapper;
    private final S3Service s3Service;

    public MypageController(MypageService mypageService,MypageMapper mypageMapper, S3Service s3Service) {
        this.mypageMapper = mypageMapper;
        this.s3Service = s3Service;
        this.mypageService = mypageService;
    }

    @GetMapping("mypage/{userId}")
    public String getMypage(@PathVariable String userId, Model model) {
        MypageDTO mypageDTO = mypageService.getMypageByUserId(userId);
        System.out.println("DTO 데이터: " + mypageDTO.getUserImgpath()); // user_imgpath 값 확인
        System.out.println("부서명: " + mypageDTO.getUserDeptName());
        System.out.println("직무명: " + mypageDTO.getUserJobName());
        System.out.println("입사일 : " + mypageDTO.getUserJncmpYmd());


        // 모델에 데이터 추가
        model.addAttribute("mypageDTO", mypageDTO);


        return "mypage/mypage";
    }
    @GetMapping("/mypage")
    public String mypage(Model model, @RequestParam("userId") String userId) {
        MypageDTO mypageDTO = mypageService.getMypageByUserId(userId);
        model.addAttribute("mypage", mypageDTO);
        return "mypage/mypage"; // Thymeleaf 파일
    }

    @PostMapping(value = "/mypage/update", consumes = "multipart/form-data")
    public ResponseEntity<String> updateMypage(@ModelAttribute MypageDTO mypageDTO,
                                               @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            // 프로필 이미지 처리
            if (profileImage != null && !profileImage.isEmpty()) {
//                String s3Url = S3Service.uploadFile(profileImage); // S3에 파일 업로드
//                mypageDTO.setUserImgpath(s3Url); // S3 URL을 DTO에 설정
            }

            // 서비스 호출로 데이터 업데이트
            mypageService.updateUserInfo(mypageDTO);
            String s3Url = s3Service.uploadFile(profileImage); // S3에 업로드하고 URL 반환
            mypageDTO.setUserImgpath(s3Url); // DTO에 S3 URL 설정
            mypageMapper.updateMypage(mypageDTO); // 데이터베이스에 저장
            return ResponseEntity.ok("수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 실패: " + e.getMessage());
        }
    }






}
