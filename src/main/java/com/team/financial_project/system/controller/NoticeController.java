package com.team.financial_project.system.controller;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.system.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("system/inquire")
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    //로그인
    private String getAuthenticatedUserId() {
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
            // 조건 검색
            list = noticeService.searchInquires(searchParams);
        }else{
            // 전체 결과
            list = noticeService.findAllList();
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
    public String noticeDetailView(@PathVariable("inqId") Long inqId, Model model) {
        InquireDTO dto = noticeService.findById(inqId);
        model.addAttribute("dto", dto);
        return "system/notice-detail";
    }

    /* 공지사항 상태 변경 */
    @GetMapping("/notice")
    public String noticeStcdChange(){
        return "system/notice-list";
    }
}
