package com.team.financial_project.promotion.controller;

import com.team.financial_project.promotion.calculator.SavingsCalculator;
import com.team.financial_project.promotion.dto.*;
import com.team.financial_project.promotion.mapper.PromotionMapper;
import com.team.financial_project.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    // MyBatis Test
    @GetMapping("/mybatisTest")
    @ResponseBody
    public String testConnection() {
        int result = promotionService.checkConnection();
        return result == 1 ? "DB 연결 성공!" : "DB 연결 실패!";
    }

    // 설계 조회 페이지
    @GetMapping("/list")
    public Object getList(
            @RequestParam(value="page", required=false, defaultValue="1") int page, // 기본 페이지
            @RequestParam(value="size", required=false, defaultValue="10") int size, // 페이지당 데이터 개수
            @RequestParam(value = "prgStcd", required = false) String prgStcd, // 진행상태 필터
            @RequestParam(value = "dsTyCd", required = false) String dsTyCd, // 상품유형 필터
            @RequestParam(value = "custNm", required = false) String custNm, // 고객명 필터
            @RequestParam(value = "userNm", required = false) String userNm, // 담당자 필터
            @RequestParam(value = "prodNm", required = false) String prodNm, // 상품명 필터
            @RequestParam(value = "sortColumn", required = false) String sortColumn, // 정렬 기준 컬럼
            @RequestParam(value = "sortDirection", required = false) String sortDirection, // 정렬 방향
            @RequestParam(value = "ajax", required = false, defaultValue = "false") boolean ajax, // AJAX 여부
            Model model) {

        // 페이지에 출력하기 위한 코드 리스트 조회 --- 진행상태 (430) 상품유형 (401)
        List<CodeDto> progressStatusList = promotionService.getCodeListByCl("430");
        List<CodeDto> productTypeList = promotionService.getCodeListByCl("401");
        model.addAttribute("progressStatusList", progressStatusList);
        model.addAttribute("productTypeList", productTypeList);

        // 전체 데이터의 개수 (필터 조건 포함)
        int totalCount = promotionService.getTotalCount(prgStcd, dsTyCd, custNm, userNm, prodNm);

        // 조건 및 페이징에 맞는 데이터 조회
        List<PromotionListDto> promotionList = promotionService.getPagedList(
                page, size, prgStcd, dsTyCd, custNm, userNm, prodNm, sortColumn, sortDirection);

        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // 페이지네이션 범위 계산
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(startPage + 4, totalPages);

        // 페이지 범위 조정 (최소 5페이지 보장)
        if (endPage - startPage < 4) {
            startPage = Math.max(1, endPage - 4);
        }

        // AJAX 요청 시 JSON 데이터 반환
        if (ajax) {
            Map<String, Object> response = new HashMap<>();
            response.put("PromotionList", promotionList);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);
            response.put("totalCount", totalCount);
            response.put("startPage", startPage);
            response.put("endPage", endPage);
            return ResponseEntity.ok(response); // JSON 데이터 반환
        }

        // HTML 렌더링 요청인 경우
        model.addAttribute("PromotionList", promotionList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prgStcd", prgStcd); // 현재 진행상태 필터 값
        model.addAttribute("dsTyCd", dsTyCd); // 현재 상품유형 필터 값
        model.addAttribute("custNm", custNm); // 현재 고객명 필터 값
        model.addAttribute("userNm", userNm); // 현재 담당자 필터 값
        model.addAttribute("prodNm", prodNm); // 현재 상품명 필터 값
        model.addAttribute("sortColumn", sortColumn); // 현재 정렬 기준 컬럼
        model.addAttribute("sortDirection", sortDirection); // 현재 정렬 방향

        return "promotion/promotionList"; // HTML 템플릿 반환
    }

    // 금융계산기 페이지
    @GetMapping("/cal")
    public String viewCalPage(
            @RequestParam(value = "codeCl", defaultValue = "430") String codeCl, // 필터 값 기본 430
            Model model) {
        // 페이지에 출력하기 위한 진행상태 조회
        List<CodeDto> progressStatusList = promotionService.getCodeListByCl(codeCl);
        model.addAttribute("progressStatusList", progressStatusList);
        return "promotion/promotionCal";
    }

    // 상품 리스트 조회
    // 적금
    @GetMapping("/cal/productList")
    @ResponseBody
    public List<ProductInfoDto> getProductList(
            @RequestParam(value = "prodCd", required = false) String prodCd,
            @RequestParam(value = "prodNm", required = false) String prodNm) {
        return promotionService.getProductList(prodCd, prodNm);
    }

    // 고객 리스트 조회
    @GetMapping("/cal/userInfoList")
    @ResponseBody
    public List<UserInfoDto> getUserInfoList(
            @RequestParam(value = "custNm", required = false) String custNm,
            @RequestParam(value = "custTelno", required = false) String custTelno) {
        return promotionService.getUserInfoList(custNm, custTelno);
    }

    // 금융계산기 적금 - 간단 계산
    @PostMapping("/cal/savg/calculate")
    @ResponseBody
    public ResponseEntity<SavingsCalDto> calculateSavings(@RequestBody SavingsCalDto requestDto) {

        // 계산 서비스 호출
        SavingsCalDto responseDto = SavingsCalculator.calculateSavings(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 금융계산기 적금 - 저장
    @PostMapping("/cal/savg/insert")
    @ResponseBody
    public ResponseEntity<String> saveSavings(@RequestBody SavingsSaveDto savingsSaveDto) {
        System.out.println("Received DTO: " + savingsSaveDto);

        // 서비스 호출
        promotionService.save(savingsSaveDto);

        return ResponseEntity.ok("저장이 완료되었습니다.");
    }

    // 진행상태 업데이트
    @PostMapping("/update-statuses")
    @ResponseBody
    public ResponseEntity<String> updateProgressStatuses() {
        try {
            int updatedCount = promotionService.updateAllProgressStatuses();
            String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (updatedCount > 0) {
                return ResponseEntity.ok("총 " + updatedCount + "건의 진행 상태가 " + formattedDateTime + " 기준으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.ok("업데이트가 필요한 진행 상태가 없습니다. (" + formattedDateTime + ")");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("진행 상태를 갱신하는 중 오류가 발생했습니다.");
        }
    }
}


