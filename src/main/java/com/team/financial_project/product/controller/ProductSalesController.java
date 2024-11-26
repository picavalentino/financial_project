package com.team.financial_project.product.controller;

import com.team.financial_project.dto.TotalSalesDTO;
import com.team.financial_project.product.service.ProductSalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
@RequestMapping("/product")
@Slf4j
public class ProductSalesController {
    private final ProductSalesService productSalesService;

    public ProductSalesController(ProductSalesService productSalesService) {
        this.productSalesService = productSalesService;
    }

    @GetMapping("/sales")
    public String handleSales(
            @RequestParam(required = false) String dsgnDsTy,
            @RequestParam(required = false) String prodNm,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String staffName,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String searchBgnYmd,
            @RequestParam(required = false) String searchEndYmd,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        log.info("### Request Parameters:");
        log.info("dsgnDsTy (상품 유형): {}", dsgnDsTy);
        log.info("prodNm (상품명): {}", prodNm);
        log.info("userId (담당자 ID): {}", userId);
        log.info("year (년도): {}", year);
        log.info("month (월): {}", month);
        log.info("searchBgnYmd (검색 시작일): {}", searchBgnYmd);
        log.info("searchEndYmd (검색 종료일): {}", searchEndYmd);
        log.info("page (페이지 번호): {}", page);

        LocalDate startOfMonth = null;
        LocalDate endOfMonth = null;
        StringBuilder periodBuilder = new StringBuilder();

        try {
            // 기간별 조회
            if (searchBgnYmd != null && !searchBgnYmd.isEmpty() && searchEndYmd != null && !searchEndYmd.isEmpty()) {
                startOfMonth = LocalDate.parse(searchBgnYmd);
                endOfMonth = LocalDate.parse(searchEndYmd);
                periodBuilder.append(searchBgnYmd).append(" ~ ").append(searchEndYmd);
            }
            // 연도별 조회
            else if (year != null && (month == null || month.isEmpty())) {
                startOfMonth = LocalDate.of(Integer.parseInt(year), 1, 1);
                endOfMonth = LocalDate.of(Integer.parseInt(year), 12, 31);
                periodBuilder.append(year).append("년");
            }
            // 연도와 월별 조회
            else if (year != null && month != null && !month.isEmpty()) {
                int parsedMonth = Integer.parseInt(month);
                startOfMonth = LocalDate.of(Integer.parseInt(year), parsedMonth, 1);
                endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
                periodBuilder.append(year).append("년 ").append(month).append("월");
            }
            // 기본 조회 (이번 달)
            else if (dsgnDsTy == null && prodNm == null && userId == null && searchBgnYmd == null && searchEndYmd == null && year == null && month == null) {
                startOfMonth = LocalDate.now().withDayOfMonth(1); // 이번 달 첫째 날
                endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
                periodBuilder.append(startOfMonth.toString().substring(0, 7)); // "YYYY-MM"
            }

            // 검색 조건만 입력되었는데 기간이 없는 경우
            if ((userId != null || dsgnDsTy != null || prodNm != null)
                    && (searchBgnYmd == null || searchEndYmd == null)) {
                model.addAttribute("errorMessage", "검색 조건을 입력할 경우 날짜 조건을 함께 선택하세요.");
                return "product/product-sales-main";
            }

            // 매출 데이터 조회
            List<TotalSalesDTO> salesData;
            if (dsgnDsTy == null && prodNm == null && userId == null) {
                // 기본 조회
                salesData = productSalesService.thisMonthSales(startOfMonth, endOfMonth);
            } else {
                // 조건 검색
                salesData = productSalesService.findSalesByConditions(startOfMonth, endOfMonth, userId, dsgnDsTy, prodNm);
            }

            // 조건 추가
            if (dsgnDsTy != null && !dsgnDsTy.isEmpty()) {
                switch (dsgnDsTy) {
                    case "1" -> periodBuilder.append(" - 적금");
                    case "2" -> periodBuilder.append(" - 목돈");
                    case "3" -> periodBuilder.append(" - 예금");
                    case "4" -> periodBuilder.append(" - 대출");
                }
            }
            if (prodNm != null && !prodNm.isEmpty()) {
                periodBuilder.append(" - ").append(prodNm);
            }
            if (userId != null && !userId.isEmpty()) {
                periodBuilder.append(" - ").append(staffName);
            }

            // 모델에 추가
            model.addAttribute("periodFilter", periodBuilder.toString());


            model.addAttribute("salesData", salesData);
            return "product/product-sales-main";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "product/product-sales-main";
        }
    }

    private int getLastDayOfMonth(String year, String month) {
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        return YearMonth.of(y, m).lengthOfMonth();
    }

}
