package com.team.financial_project.inquire.controller;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.inquire.service.InquireService;
import com.team.financial_project.main.service.PaginationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/inquire")
public class InquireController {
    private final InquireService inquireService;
    private final PaginationService paginationService;

    public InquireController(InquireService inquireService, PaginationService paginationService) {
        this.inquireService = inquireService;
        this.paginationService = paginationService;
    }

    //로그인
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
    }

    //게시글 전체 목록
    @GetMapping("/list")
    public String viewInquireList(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        return searchInquires(null, null, null, null, page, model);
    }

    //게시글 조건 검색
    @GetMapping("/search")
    public String searchInquires(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keywordType", required = false) String keywordType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "inqCreateAt", required = false) String createAt,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) {
        int pageSize = 10;
        int totalInquires = inquireService.countSearchInquires(category, keywordType, keyword, createAt);

        // `totalPages` 계산
        int totalPages = (int) Math.ceil((double) totalInquires / pageSize);
        if (totalPages == 0) {
            totalPages = 1; // 최소 1페이지로 설정
        }

        // 데이터 추가
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("inquiries", inquireService.searchInquires(category, keywordType, keyword, createAt, page, pageSize));

        return "/inquire/inquire-list";
    }


    //게시글 상세보기
    @GetMapping("/detail/{inqId}")
    public String viewInquireDetail(@PathVariable Long inqId, Model model) {
        // inqId로 게시글 DTO 조회
        InquireDTO inquireDTO = inquireService.getInquireById(inqId);
        //카테고리 이름 바꾸기
        if(inquireDTO.getInqCategory().equals("1")){
            inquireDTO.setInqCategory("공지사항");
        }else if(inquireDTO.getInqCategory().equals("2")){
            inquireDTO.setInqCategory("시스템 관련 문의");
        }else if(inquireDTO.getInqCategory().equals("3")){
            inquireDTO.setInqCategory("기타 건의사항");
        }

        // 조회한 DTO를 Model에 추가
        model.addAttribute("inquire", inquireDTO);
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
