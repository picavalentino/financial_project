package com.team.financial_project.mypage.controller;

import com.team.financial_project.login.service.RegisterService;
import com.team.financial_project.main.service.S3Service;
import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.mypage.mapper.MypageMapper;
import com.team.financial_project.mypage.service.MypageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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



    @GetMapping("/mypage")
    public String getMypage(Model model) {



        // 1. 인증된 사용자 ID 가져오기
        String userId = getAuthenticatedUserId();


        // 2. 서비스 호출하여 사용자 정보 조회
        MypageDTO mypageDTO = mypageService.getMypageByUserId(userId);
        if (mypageDTO == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }
        // 3. 모델에 데이터 추가
        model.addAttribute("mypageDTO", mypageDTO);
        System.out.println("DTO 데이터: " + mypageDTO);
        System.out.println("이미지 경로: " + mypageDTO.getUserImgpath());


        // 4. 뷰 반환
        return "mypage/mypage"; // mypage.html로 데이터 전달
    }

    // 인증된 사용자 ID를 가져오는 메서드
    private String getAuthenticatedUserId() {
        // Spring Security에서 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // userId 반환 (로그인 시 사용한 ID)
    }

    @PostMapping("/mypage/update")
    @ResponseBody
    public ResponseEntity<?> updateMypage(@RequestParam("userId") String userId,
                                          @RequestParam("userEmail") String userEmail,
                                          @RequestParam("phonePart1") String phonePart1,
                                          @RequestParam("phonePart2") String phonePart2,
                                          @RequestParam("phonePart3") String phonePart3,
                                          @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            // 전화번호 합치기
            String userTelno = phonePart1 + "-" + phonePart2 + "-" + phonePart3;

            // MypageDTO 생성 및 값 설정
            MypageDTO mypageDTO = new MypageDTO();
            mypageDTO.setUserId(userId);
            mypageDTO.setUserEmail(userEmail);
            mypageDTO.setUserTelno(userTelno);

            // 기존 이미지 유지 처리
            MypageDTO currentData = mypageService.getMypageByUserId(userId);
            if (profileImage != null && !profileImage.isEmpty()) {
                // 이미지가 업로드된 경우 처리
                String fileUrl = s3Service.uploadFile(profileImage);
                mypageDTO.setUserImgpath(fileUrl);
            } else {
                // 이미지가 업로드되지 않은 경우 기존 값 유지
                mypageDTO.setUserImgpath(currentData.getUserImgpath());
            }

            // 사용자 정보 업데이트
            boolean isUpdated = mypageService.updateUserInfo(mypageDTO);
            if (isUpdated) {
                return ResponseEntity.ok("마이페이지 정보가 성공적으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("업데이트 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다.");
        }
    }








    @PostMapping("/mypage/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestParam("newPassword") String newPassword,
                                            @RequestParam("confirmPassword") String confirmPassword    ) {
        try {
            // 비밀번호 일치 여부 확인
            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }

            // 현재 사용자 ID 가져오기 (예: SecurityContextHolder 사용)
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();


            // 서비스 호출
            boolean isUpdated = mypageService.updateUserPassword(userId, newPassword);

            if (isUpdated) {
                return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }









}
