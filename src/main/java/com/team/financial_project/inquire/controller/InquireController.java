package com.team.financial_project.inquire.controller;

import com.team.financial_project.dto.InquireCommentDTO;
import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.inquire.service.InquireService;
import com.team.financial_project.main.service.PaginationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
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
    public String viewInquireDetail(@PathVariable("inqId") Integer inqId, Model model) {
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

        //작성자 이름 바꾸기
        if(inquireDTO.getInqAnonym().equals("1")){
            inquireDTO.setUserId("익명");
        }else if (inquireDTO.getInqAnonym().equals("0")){
            inquireDTO.setUserId(inquireService.getUserName(inquireDTO.getUserId()));
        }

        //작성시간 - 수정했으면 작성시간 변경
        if(inquireDTO.getInqUpdateAt() != null){
            inquireDTO.setInqCreateAt(inquireDTO.getInqUpdateAt());
        }

        //첨부 파일 이름 변경
        List<String> fileNames = new ArrayList<>();
        String basePath = "https://codechef.s3.ap-northeast-2.amazonaws.com/";
        // 각 필드를 수동으로 처리
        if (inquireDTO.getInqAttachFile1() != null && inquireDTO.getInqAttachFile1().startsWith(basePath)) {
            fileNames.add(inquireDTO.getInqAttachFile1().substring(basePath.length()));
        }
        if (inquireDTO.getInqAttachFile2() != null && inquireDTO.getInqAttachFile2().startsWith(basePath)) {
            fileNames.add(inquireDTO.getInqAttachFile2().substring(basePath.length()));
        }
        if (inquireDTO.getInqAttachFile3() != null && inquireDTO.getInqAttachFile3().startsWith(basePath)) {
            fileNames.add(inquireDTO.getInqAttachFile3().substring(basePath.length()));
        }
        if (inquireDTO.getInqAttachFile4() != null && inquireDTO.getInqAttachFile4().startsWith(basePath)) {
            fileNames.add(inquireDTO.getInqAttachFile4().substring(basePath.length()));
        }
        if (inquireDTO.getInqAttachFile5() != null && inquireDTO.getInqAttachFile5().startsWith(basePath)) {
            fileNames.add(inquireDTO.getInqAttachFile5().substring(basePath.length()));
        }

        //댓글 가져오기
        List<InquireCommentDTO> comments = inquireService.getCommentsByInqId(inqId);

        // 댓글 중 관리자 댓글이 있는지 확인
        boolean hasAdminComment = false;
        for (InquireCommentDTO comment : comments) {
            if ("1".equals(comment.getUserAuthCd()) || "2".equals(comment.getUserAuthCd())) {
                hasAdminComment = true;
                break;
            }
        }
        // 관리자 댓글이 있다면 inqReply를 1로 업데이트
        if (hasAdminComment && !"1".equals(inquireDTO.getInqReply())) {
            inquireService.updateInqReply(inqId, "1");
        }

        //답변상태 텍스트 변경
        String replyText="";
        if(inquireDTO.getInqReply().equals("0")){
            replyText="답변 대기";
        }else if(inquireDTO.getInqReply().equals("1")){
            replyText="답변 완료";
        }


        log.info("==============================inquire:"+String.valueOf(inquireDTO));
        log.info("==============================fileNames:"+String.valueOf(fileNames));
        log.info("==============================comments:"+comments);

        // 조회한 DTO를 Model에 추가
        model.addAttribute("inquire", inquireDTO);
        model.addAttribute("replyText",replyText);
        model.addAttribute("fileNames", fileNames);
        model.addAttribute("comments", comments);
        return "/inquire/inquire-detail";
    }

    //게시글 상세보기 - 댓글 입력
    @PostMapping("/detail/{inqId}/comment")
    public String saveComment(
            @PathVariable("inqId") Integer inqId,
            @ModelAttribute InquireCommentDTO commentDTO,
            RedirectAttributes redirectAttributes) {
        // 로그인된 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // DTO 설정
        commentDTO.setInqId(inqId);
        commentDTO.setUserId(userId);

        // user_auth_cd 조회
        String userAuthCd = inquireService.getUserAuthCdByUserId(userId);

        // 댓글 저장
        inquireService.saveComment(commentDTO);

        // user_auth_cd가 1 또는 2인 경우 inq_reply 업데이트
        if ("1".equals(userAuthCd) || "2".equals(userAuthCd)) {
            inquireService.updateInqReply(inqId, "1");
        }

        // 리다이렉트 처리
        redirectAttributes.addFlashAttribute("message", "댓글이 성공적으로 저장되었습니다.");
        return "redirect:/inquire/detail/" + inqId;
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

    //상세페이지에서 수정으로 이동 시 비밀번호 체크
    @PostMapping("/detail/{inqId}/check-password")
    @ResponseBody
    public ResponseEntity<Void> checkPassword(
            @PathVariable("inqId") Integer inqId,
            @RequestParam("password") String password) {
        // 게시글 가져오기
        InquireDTO inquireDTO = inquireService.getInquireById(inqId);

        // 현재 로그인된 사용자 ID 가져오기
        String loggedInUserId = getAuthenticatedUserId(); // 로그인된 사용자 ID

        // 로그인 사용자와 작성자 일치 확인
        if(!inquireDTO.getUserId().equals(loggedInUserId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
        // 비밀번호 확인
        if (!inquireDTO.getInqPwd().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        return ResponseEntity.ok().build(); // 200 OK
    }


    @GetMapping("/detail/{inqId}/update")
    public String editInquire(@PathVariable("inqId") Integer inqId, Model model) {
        // 게시글 조회
        InquireDTO inquireDTO = inquireService.getInquireById(inqId);

        // 모델에 데이터 추가
        model.addAttribute("inquire", inquireDTO);

        return "/inquire/inquire-update"; // 수정 페이지로 이동
    }

    //게시글 수정
    @PostMapping("/detail/{inqId}/update")
    public ResponseEntity<?> updateInquire(
            @PathVariable("inqId") Integer inqId,
            @RequestParam("files") MultipartFile[] files, // 파일 배열
            @RequestBody InquireDTO updatedInquire) {

        // 기존 데이터 가져오기
        InquireDTO existingInquire = inquireService.getInquireById(inqId);

        // 비밀번호 확인 (프론트엔드에서 이미 확인했지만 서버에서도 이중 확인)
        if (!existingInquire.getInqPwd().equals(updatedInquire.getInqPwd())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않습니다.");
        }

        // 제목과 내용만 업데이트
        existingInquire.setInqTitle(updatedInquire.getInqTitle());
        existingInquire.setInqContent(updatedInquire.getInqContent());
        log.info("### updated inquire: "+existingInquire);
        inquireService.updateInquire(existingInquire);
        return ResponseEntity.ok("수정 완료");
    }

    //게시글 삭제
    @PostMapping("/detail/{inqId}/delete")
    public ResponseEntity<?> deleteInquire(@PathVariable Integer inqId, @RequestParam String inqPwd) {
        InquireDTO inquire = inquireService.getInquireById(inqId);

        if (!inqPwd.equals(inquire.getInqPwd())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않습니다.");
        }
        inquireService.deleteInquire(inqId);
        return ResponseEntity.ok("삭제 완료");
    }
}