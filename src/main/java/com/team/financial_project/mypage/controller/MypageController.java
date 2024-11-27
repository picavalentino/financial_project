package com.team.financial_project.mypage.controller;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.login.service.RegisterService;
import com.team.financial_project.main.service.S3Service;
import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.mypage.mapper.MypageMapper;
import com.team.financial_project.mypage.service.MypageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class MypageController{

    private final MypageService mypageService;
    private final MypageMapper mypageMapper;
    private final S3Service s3Service;
    private final RegisterService registerService;

    public MypageController(MypageService mypageService,MypageMapper mypageMapper, S3Service s3Service, RegisterService registerService) {
        this.mypageMapper = mypageMapper;
        this.s3Service = s3Service;
        this.mypageService = mypageService;
        this.registerService = registerService;
    }

    @GetMapping("/mypage/{userId}")
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


    @PostMapping("/mypage/update")
    @ResponseBody
    public String updateUser(@RequestBody MypageDTO mypageDTO) {
        try {
            // 디버깅용으로 받은 데이터 출력
            System.out.println("Received Data: " + mypageDTO);

            // 필요한 필드만 업데이트 처리
            boolean isUpdated = mypageService.updateUserInfo(mypageDTO);
            if (isUpdated) {
                return "수정 완료";
            } else {
                return "수정 실패";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "서버 오류 발생";
        }
    }

//    @PostMapping("/mypage/change-password")
//    @ResponseBody
//    public  changePassword(@RequestBody MypageDTO mypageDTO) {
//        try {
//            System.out.println("수정 요청 데이터: " + mypageDTO); // 디버깅용
//
//            // 필요한 필드만 업데이트 처리
//            boolean isUpdated = mypageService.updateUserInfo(mypageDTO);
//            if (isUpdated) {
//                return ResponseEntity.ok("수정 완료");
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 실패");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
//        }
//    }







}
