package com.team.financial_project.system.controller;

import com.team.financial_project.dto.InquireCommentDTO;
import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.inquire.service.InquireService;
import com.team.financial_project.system.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("system/inquire")
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;
    private final InquireService inquireService;

    public NoticeController(NoticeService noticeService, InquireService inquireService) {
        this.noticeService = noticeService;
        this.inquireService = inquireService;
    }

    //로그인
    private String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
    }

    @GetMapping("")
    public Object noticeManageView(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "8") int size,
            @RequestParam(required = false) String inqCategory,
            @RequestParam(required = false) String keywordType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String inqCreateAt,
            @RequestParam(value = "sortColumn", required = false) String sortColumn,
            @RequestParam(value = "sortDirection", required = false) String sortDirection,
            @RequestParam(value = "ajax", required = false, defaultValue = "false") boolean ajax,
            Model model, HttpServletRequest request
    ) {
        // 요청 파라미터 확인 로그
        System.out.println("### 요청 파라미터 확인 ###");
        System.out.println("page=" + page + ", size=" + size);
        System.out.println("inqCategory=" + inqCategory + ", keywordType=" + keywordType);
        System.out.println("keyword=" + keyword + ", inqCreateAt=" + inqCreateAt);
        System.out.println("sortColumn=" + sortColumn + ", sortDirection=" + sortDirection);
        System.out.println("ajax=" + ajax);

        try {
            // 서비스 호출 및 데이터 처리
            Map<String, Object> result = noticeService.getNotices(
                    inqCategory, keywordType, keyword, inqCreateAt,
                    Math.max(page, 1), Math.max(size, 8), // 기본값 설정
                    sortColumn, sortDirection
            );

            // 데이터 추출
            List<InquireDTO> paginatedList = (List<InquireDTO>) result.get("list");
            int totalPages = (int) result.get("totalPages");
            int totalItems = (int) result.get("totalItems");
            System.out.println("totalItems: "+totalItems);

            // AJAX 요청 처리
            if (ajax) {
                Map<String, Object> response = new HashMap<>();
                response.put("list", paginatedList);
                response.put("totalPages", totalPages);
                response.put("currentPage", page);
                response.put("totalItems", totalItems);
                return ResponseEntity.ok(response);
            }

            // 일반 요청 처리
            model.addAttribute("list", paginatedList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            return "system/notice-list";
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            if (ajax) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "An error occurred while processing the request."));
            }
            model.addAttribute("error", "An error occurred while processing the request.");
            return "system/notice-list";
        }
    }

    /* 게시글 상세 */
    @GetMapping("/detail/{inqId}")
    public String noticeDetailView(@PathVariable("inqId") Integer inqId, Model model) {
        // 게시글 찾기
        InquireDTO dto = inquireService.getInquireById(inqId);

        // 현재 로그인된 사용자 ID 가져오기
        String loggedInUserId = getAuthenticatedUserName(); // 로그인된 사용자 ID
        boolean isValid = loggedInUserId.equals(dto.getUserId()); // 작성자와 로그인된 사용자가 같은지 확인

        // 작성자 확인 결과를 뷰에 전달
        model.addAttribute("isValid", isValid);

        // dto 작성자 이름 변환
        dto.setUserId(inquireService.getUserName(dto.getUserId()));

        //첨부 파일 이름 변경
        List<String> fileNames = new ArrayList<>();
        String basePath = "https://codechef.s3.ap-northeast-2.amazonaws.com/";

        // 각 필드를 수동으로 처리
        if (dto.getInqAttachFile1() != null && dto.getInqAttachFile1().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile1().substring(basePath.length()));
        }
        if (dto.getInqAttachFile2() != null && dto.getInqAttachFile2().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile2().substring(basePath.length()));
        }
        if (dto.getInqAttachFile3() != null && dto.getInqAttachFile3().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile3().substring(basePath.length()));
        }
        if (dto.getInqAttachFile4() != null && dto.getInqAttachFile4().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile4().substring(basePath.length()));
        }
        if (dto.getInqAttachFile5() != null && dto.getInqAttachFile5().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile5().substring(basePath.length()));
        }
        log.info("Attach Files: {}", fileNames);

        //댓글 가져오기
        List<InquireCommentDTO> comments = inquireService.getCommentsByInqId(inqId);

        model.addAttribute("inquire", dto);
        model.addAttribute("fileNames", fileNames); // 정리된 첨부파일 리스트 전달
        model.addAttribute("comments", comments);
        return "system/notice-detail";
    }

    /* 공지사항 상태 변경 - 고정 */
    @PostMapping("/notice-register/{inqId}")
    public ResponseEntity<String> registerNotice(@PathVariable("inqId") Integer inqId) {
        noticeService.updateNoticeStatus(inqId, "1"); // 1: 공지 등록
        return ResponseEntity.ok("공지 등록 완료");
    }
    /* 공지사항 상태 변경 - 고정 해제 */
    @PostMapping("/notice-cancel/{inqId}")
    public ResponseEntity<String> cancelNotice(@PathVariable("inqId") Integer inqId) {
        noticeService.updateNoticeStatus(inqId, "0"); // 2: 공지 해제
        return ResponseEntity.ok("공지 해제 완료");
    }

    // 신규 공지사항 등록
    @GetMapping("insert")
    public String noticeInsertView(Model model){
        InquireDTO dto = new InquireDTO();
        dto.setUserId(getAuthenticatedUserName());
        dto.setInqAnonym("0");
        dto.setInqCategory("1");
        String userName = inquireService.getUserName(dto.getUserId());
        model.addAttribute("dto", dto);
        model.addAttribute("userName", userName);
        return "system/notice-insert";
    }
    @PostMapping("insert")
    public String noticeInsertProc(@ModelAttribute InquireDTO inquireDTO,
                                   @RequestParam("files") MultipartFile[] files) {
        try {
            log.info("### Received DTO: "+inquireDTO);
            // 파일 업로드 처리
            for (int i = 0; i < files.length && i < 5; i++) {
                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    // S3 업로드 서비스 호출
                    String fileUrl = inquireService.uploadFileToS3(file);
                    // 업로드된 파일 URL을 DTO에 설정
                    switch (i) {
                        case 0 -> inquireDTO.setInqAttachFile1(fileUrl);
                        case 1 -> inquireDTO.setInqAttachFile2(fileUrl);
                        case 2 -> inquireDTO.setInqAttachFile3(fileUrl);
                        case 3 -> inquireDTO.setInqAttachFile4(fileUrl);
                        case 4 -> inquireDTO.setInqAttachFile5(fileUrl);
                    }
                }
            }
            // 게시글 저장
            inquireService.saveInquire(inquireDTO);
            log.info("공지사항 등록 성공: " + inquireDTO);
            return "redirect:/system/inquire";
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: ", e);
            return "redirect:/system/inquire/insert?error=true";
        } catch (Exception e) {
            log.error("게시글 저장 중 오류 발생: ", e);
            return "redirect:/system/inquire/insert?error=true";
        }
    }

    // 공지 게시글 수정
    @GetMapping("/detail/{inqId}/update")
    public String updateNoticeView(@PathVariable("inqId") Integer inqId, Model model) {
        InquireDTO dto = noticeService.findById(inqId);

        List<String> fileNames = new ArrayList<>();
        String basePath = "https://codechef.s3.ap-northeast-2.amazonaws.com/";

        // 각 필드를 수동으로 처리하여 파일 이름만 추출
        if (dto.getInqAttachFile1() != null && dto.getInqAttachFile1().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile1().substring(basePath.length()));
        }
        if (dto.getInqAttachFile2() != null && dto.getInqAttachFile2().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile2().substring(basePath.length()));
        }
        if (dto.getInqAttachFile3() != null && dto.getInqAttachFile3().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile3().substring(basePath.length()));
        }
        if (dto.getInqAttachFile4() != null && dto.getInqAttachFile4().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile4().substring(basePath.length()));
        }
        if (dto.getInqAttachFile5() != null && dto.getInqAttachFile5().startsWith(basePath)) {
            fileNames.add(dto.getInqAttachFile5().substring(basePath.length()));
        }

        // 모델에 파일 정보와 게시글 데이터를 추가
        model.addAttribute("inquire", dto);
        model.addAttribute("fileNames", fileNames); // URL과 파일 이름 리스트
        return "system/notice-update";
    }
    @PostMapping("/detail/{inqId}/update")
    public ResponseEntity<?> updateNotice(
            @PathVariable("inqId") Integer inqId,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "deletedFiles", required = false) List<String> deletedFiles,
            @ModelAttribute InquireDTO updatedInquire) throws IOException {

        // 기존 데이터 가져오기
        InquireDTO existingInquire = inquireService.getInquireById(inqId);

        // 비밀번호 확인
        if (!existingInquire.getInqPwd().equals(updatedInquire.getInqPwd())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않습니다.");
        }

        // **1. 삭제 요청된 파일 처리**
        if (deletedFiles != null && !deletedFiles.isEmpty()) {
            log.info("Deleted Files: {}", deletedFiles);
            for (String fileName : deletedFiles) {
                // 파일 이름 비교 (URL에서 파일 이름만 추출)
                if (existingInquire.getInqAttachFile1() != null
                        && existingInquire.getInqAttachFile1().endsWith(fileName)) {
                    log.info("Deleting file from field inqAttachFile1: {}", fileName);
                    existingInquire.setInqAttachFile1(null);
                } else if (existingInquire.getInqAttachFile2() != null
                        && existingInquire.getInqAttachFile2().endsWith(fileName)) {
                    log.info("Deleting file from field inqAttachFile2: {}", fileName);
                    existingInquire.setInqAttachFile2(null);
                } else if (existingInquire.getInqAttachFile3() != null
                        && existingInquire.getInqAttachFile3().endsWith(fileName)) {
                    log.info("Deleting file from field inqAttachFile3: {}", fileName);
                    existingInquire.setInqAttachFile3(null);
                } else if (existingInquire.getInqAttachFile4() != null
                        && existingInquire.getInqAttachFile4().endsWith(fileName)) {
                    log.info("Deleting file from field inqAttachFile4: {}", fileName);
                    existingInquire.setInqAttachFile4(null);
                } else if (existingInquire.getInqAttachFile5() != null
                        && existingInquire.getInqAttachFile5().endsWith(fileName)) {
                    log.info("Deleting file from field inqAttachFile5: {}", fileName);
                    existingInquire.setInqAttachFile5(null);
                }
            }
        }

        // **2. 새로 업로드된 파일 처리**
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = inquireService.uploadFileToS3(file); // S3 업로드 처리

                    // 빈 필드에 새 파일 추가
                    if (existingInquire.getInqAttachFile1() == null) {
                        existingInquire.setInqAttachFile1(fileUrl);
                    } else if (existingInquire.getInqAttachFile2() == null) {
                        existingInquire.setInqAttachFile2(fileUrl);
                    } else if (existingInquire.getInqAttachFile3() == null) {
                        existingInquire.setInqAttachFile3(fileUrl);
                    } else if (existingInquire.getInqAttachFile4() == null) {
                        existingInquire.setInqAttachFile4(fileUrl);
                    } else if (existingInquire.getInqAttachFile5() == null) {
                        existingInquire.setInqAttachFile5(fileUrl);
                    }
                }
            }
        }

        // **3. 제목과 내용 업데이트**
        existingInquire.setInqTitle(updatedInquire.getInqTitle());
        existingInquire.setInqContent(updatedInquire.getInqContent());

        // **4. 변경사항 저장**
        log.info("Updated Inquire DTO: {}", existingInquire);
        inquireService.updateInquire(existingInquire);

        return ResponseEntity.ok("수정 완료");
    }

    // 공지 게시글 삭제
    @PostMapping("/detail/{inqId}/delete")
    public ResponseEntity<?> deleteInquire(@PathVariable Integer inqId, @RequestParam String inqPwd) {
        InquireDTO inquire = noticeService.findById(inqId);

        if (!inqPwd.equals(inquire.getInqPwd())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않습니다.");
        }
        inquireService.deleteInquire(inqId);
        return ResponseEntity.ok("삭제 완료");
    }
}
