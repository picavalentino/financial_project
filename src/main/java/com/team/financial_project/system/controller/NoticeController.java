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
            @RequestParam(value = "size", required = false, defaultValue = "8") int size, // 페이지당 데이터 개수
            @RequestParam(required = false) String inqCategory,
            @RequestParam(required = false) String keywordType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String inqCreateAt,
            @RequestParam(value = "sortColumn", required = false) String sortColumn, // 정렬 기준 컬럼
            @RequestParam(value = "sortDirection", required = false) String sortDirection, // 정렬 방향
            @RequestParam(value = "ajax", required = false, defaultValue = "false") boolean ajax, // AJAX 여부
            Model model, HttpServletRequest request
    ) {
        System.out.println("### search conditons: inqCategory="+inqCategory+", keyword="+keyword+", keywordType="+keywordType+", inqCreateAt="+inqCreateAt);
        
        // 조건 설정
        Map<String, Object> searchParams = new HashMap<>();
        boolean hasSearchParams = false;

        // 카테고리
        if(inqCategory != null && !inqCategory.trim().isEmpty()){
            searchParams.put("inqCategory", inqCategory.trim());
            hasSearchParams = true;
        }
        // 키워드 검색
        if(keywordType != null && keyword != null){
            switch (keywordType){
                case "inqTitle":
                    searchParams.put("inqTitle", keyword);
                    hasSearchParams = true;
                    break;
                case "userName":
                    searchParams.put("userName", keyword);
                    break;
                default:
                    System.out.println("Invalid keywordType: "+keywordType);
                    break;
            }
        }
        //작성일
        if(inqCreateAt != null && !inqCategory.trim().isEmpty()){
            searchParams.put("inqCreateAt", inqCreateAt.trim());
            hasSearchParams = true;
        }

        // 검색 결과 가져오기
        List<InquireDTO> list;
        if(hasSearchParams){
            list = noticeService.searchInquires(searchParams);  // 조건 검색
        }else{
            list = noticeService.findAllList();  // 전체 결과
        }
        log.info("### Retrieved list in Controller: {}", list);
        
        // requestURI에 페이지 url 포함
        String requestURI = request.getRequestURI();
        model.addAttribute("requestURI", requestURI);
        
        // 정렬
        if (sortColumn != null && sortDirection != null) {
            Comparator<InquireDTO> comparator = null;
            switch (sortColumn) {
                case "inqId":
                    comparator = Comparator.comparing(InquireDTO::getInqId);
                    break;
                case "inqCategory":
                    comparator = Comparator.comparing(InquireDTO::getInqCategory);
                    break;
                case "userId":
                    comparator = Comparator.comparing(InquireDTO::getUserId);
                    break;
                case "inqTitle":
                    comparator = Comparator.comparing(InquireDTO::getInqTitle);
                    break;
                case "inqCreateAt":
                    comparator = Comparator.comparing(InquireDTO::getInqCreateAt);
                    break;
                case "inqCheck": // 조회수 -> integer로 변경
                    comparator = Comparator.comparing(InquireDTO::getInqCheck);
                    break;
                case "inqReply":
                    comparator = Comparator.comparing(InquireDTO::getInqReply);
                    break;
                case "inqNotice":
                    comparator = Comparator.comparing(InquireDTO::getInqNotice);
                    break;
                default:
                    // 기본 정렬: 상품명
                    comparator = Comparator.comparing(InquireDTO::getInqId);
                    break;
            }
            // 내림차순 정렬
            if (comparator != null && "desc".equals(sortDirection)) {
                comparator = comparator.reversed();
            }
            if (comparator != null) {
                list.sort(comparator);
            }
        }

        int totalItems = list.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<InquireDTO> paginatedList = list.subList(startIndex, endIndex);

        if (ajax) {
            Map<String, Object> response = new HashMap<>();
            response.put("list", paginatedList);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);
            response.put("totalItems", totalItems); // 총 상품 개수 추가
            return ResponseEntity.ok(response); // JSON 데이터 반환
        }

        model.addAttribute("list", paginatedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("productSize", totalItems);
        return "system/notice-list";
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
        noticeService.updateNoticeStatus(inqId, 1); // 1: 공지 등록
        return ResponseEntity.ok("공지 등록 완료");
    }
    /* 공지사항 상태 변경 - 고정 해제 */
    @PostMapping("/notice-cancel/{inqId}")
    public ResponseEntity<String> cancelNotice(@PathVariable("inqId") Integer inqId) {
        noticeService.updateNoticeStatus(inqId, 2); // 2: 공지 해제
        return ResponseEntity.ok("공지 해제 완료");
    }

    // 신규 공지사항 등록
    @GetMapping("insert")
    public String noticeInsertView(Model model){
        InquireDTO dto = new InquireDTO();
        dto.setUserId(getAuthenticatedUserName());
        dto.setInqAnonym("0");
        model.addAttribute("dto", dto);
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
    public String updateNoticeView(@PathVariable("inqId") Integer inqId, Model model){
        InquireDTO dto = noticeService.findById(inqId);
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
        model.addAttribute("inquire", dto);
        model.addAttribute("fileNames", fileNames);
        return "system/notice-update";
    }
    @PostMapping("/detail/{inqId}/update")
    public ResponseEntity<?> updateInquire(
            @PathVariable("inqId") Integer inqId,
            @RequestParam(value = "files", required = false) MultipartFile[] files, // 파일 배열
            @ModelAttribute InquireDTO updatedInquire) throws IOException {

        // 기존 데이터 가져오기
        InquireDTO existingInquire = inquireService.getInquireById(inqId);

        // 비밀번호 확인
        if (!existingInquire.getInqPwd().equals(updatedInquire.getInqPwd())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않습니다.");
        }

        // 삭제 요청된 파일 필드 처리
        if (updatedInquire.getInqAttachFile1() == null) {
            existingInquire.setInqAttachFile1(null);
        }
        if (updatedInquire.getInqAttachFile2() == null) {
            existingInquire.setInqAttachFile2(null);
        }
        if (updatedInquire.getInqAttachFile3() == null) {
            existingInquire.setInqAttachFile3(null);
        }
        if (updatedInquire.getInqAttachFile4() == null) {
            existingInquire.setInqAttachFile4(null);
        }
        if (updatedInquire.getInqAttachFile5() == null) {
            existingInquire.setInqAttachFile5(null);
        }

        // 파일 업로드 처리
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // S3 업로드 서비스 호출
                    String fileUrl = inquireService.uploadFileToS3(file);

                    // 업로드된 파일을 빈 필드에 설정
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

        // 제목과 내용 업데이트
        existingInquire.setInqTitle(updatedInquire.getInqTitle());
        existingInquire.setInqContent(updatedInquire.getInqContent());

        // 변경사항 저장
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
