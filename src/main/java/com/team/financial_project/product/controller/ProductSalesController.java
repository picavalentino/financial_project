package com.team.financial_project.product.controller;

import com.team.financial_project.dto.ProductSalesDTO;
import com.team.financial_project.dto.TotalSalesDTO;
import com.team.financial_project.product.service.ProductSalesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String viewProductSales(Model model){
        // 이번 달 시작과 종료 날짜를 계산
        /*LocalDate now = LocalDate.now();
        String startOfMonth = now.withDayOfMonth(1).toString(); // 이번 달 1일
        String endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).toString(); // 이번 달 마지막 날*/
        // 현재 월의 시작과 끝 날짜를 LocalDate로 생성
        // 이번 달 시작과 종료 날짜를 계산
        LocalDate startOfMonth = LocalDate.of(2023, 11, 1); // 2023년 11월 1일
        LocalDate endOfMonth = LocalDate.of(2023, 11, 30); // 2023년 11월 30일

        // 서비스 호출
        List<TotalSalesDTO> list = productSalesService.thisMonthSales(startOfMonth, endOfMonth);
        log.info("### sales controller - total sales: " + list);

        // 연도와 월 정보만 전달
        String yearMonth = startOfMonth.toString().substring(0, 7); // "YYYY-MM"
        model.addAttribute("salesData", list);
        model.addAttribute("periodFilter", yearMonth);

        return "product/product-sales-main";
    }

    @GetMapping("/sales/search")
    public String salesByKeyword(
            @RequestParam(required = false) String prodTyCd,
            @RequestParam(required = false) String prodNm,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String searchBgnYmd,
            @RequestParam(required = false) String searchEndYmd,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        LocalDate startOfMonth = null;
        LocalDate endOfMonth = null;

        try {
            // 기간별 조회
            if (searchBgnYmd != null && !searchBgnYmd.isEmpty() && searchEndYmd != null && !searchEndYmd.isEmpty()) {
                startOfMonth = LocalDate.parse(searchBgnYmd);
                endOfMonth = LocalDate.parse(searchEndYmd);
                Map<String, String> period = new HashMap<>();
                period.put("startDate", searchBgnYmd);
                period.put("endDate", searchEndYmd);
                model.addAttribute("periodFilter", period);
            }
            // 연도별 조회
            else if (year != null && (month == null || month.isEmpty())) {
                startOfMonth = LocalDate.of(Integer.parseInt(year), 1, 1);
                endOfMonth = LocalDate.of(Integer.parseInt(year), 12, 31);
                model.addAttribute("periodFilter", year);
            }
            // 연도와 월별 조회
            else if (year != null && month != null && !month.isEmpty()) {
                int parsedMonth = Integer.parseInt(month);
                startOfMonth = LocalDate.of(Integer.parseInt(year), parsedMonth, 1);
                endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
                model.addAttribute("periodFilter", year+"년 "+month+"월");
            }

            // 파라미터 유효성 검증
            if (startOfMonth == null || endOfMonth == null) {
                throw new IllegalArgumentException("기간 조건이 올바르지 않습니다.");
            }

            // 매출 데이터 조회
            List<TotalSalesDTO> salesData = productSalesService.thisMonthSales(startOfMonth, endOfMonth);
            model.addAttribute("salesData", salesData);
            return "product/product-sales-main";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "product/product-sales-main"; // 메인 페이지로 이동
        }
    }

    private int getLastDayOfMonth(String year, String month) {
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        return YearMonth.of(y, m).lengthOfMonth();
    }

}
