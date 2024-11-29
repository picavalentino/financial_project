package com.team.financial_project.inquire.controller;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.inquire.service.InquireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/inquire")
public class InquireController {
    private final InquireService inquireService;

    public InquireController(InquireService inquireService) {
        this.inquireService = inquireService;
    }

    //로그인
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
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

    //게시글 상세보기
    @GetMapping("/detail")
    public String viewInquireDetailtest() {
        return "/inquire/inquire-detail";
    }

    //신규 게시글 등록
    @GetMapping("/insert")
    public String insertInquireView(Model model){
        // 로그인된 사용자 ID 설정
        String userId = getAuthenticatedUserId();
        String userName = inquireService.getUserName(userId);
        model.addAttribute("userName", userName);
        return "/inquire/inquire-insert";
    }

    @PostMapping("/insert")
    public String insertInquireProc(InquireDTO inquireDTO, @RequestParam("files") MultipartFile[] files) {
        // 로그인된 사용자 ID 설정
        String userId = getAuthenticatedUserId();
        inquireDTO.setUserId(userId);

        // 파일 업로드 처리
        try {
            for (int i = 0; i < files.length && i < 5; i++) {
                if (!files[i].isEmpty()) {
                    String fileUrl = inquireService.uploadFileToS3(files[i]);
                    switch (i) {
                        case 0 -> inquireDTO.setInqAttachFile1(fileUrl);
                        case 1 -> inquireDTO.setInqAttachFile2(fileUrl);
                        case 2 -> inquireDTO.setInqAttachFile3(fileUrl);
                        case 3 -> inquireDTO.setInqAttachFile4(fileUrl);
                        case 4 -> inquireDTO.setInqAttachFile5(fileUrl);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/inquire/insert?error=true";
        }

        // 게시글 저장
        inquireService.saveInquire(inquireDTO);
        log.info(String.valueOf(inquireDTO));
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
