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
import java.util.List;

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
        LocalDate startOfMonth = LocalDate.of(2023, 11, 1);
        LocalDate endOfMonth = LocalDate.of(2023, 11, 30);

        // 서비스 호출
        List<TotalSalesDTO> list = productSalesService.thisMonthSales(startOfMonth, endOfMonth);
        log.info("### sales controller - total sales: " + list);

        // 연도와 월 정보만 전달
        String yearMonth = startOfMonth.toString().substring(0, 7); // "YYYY-MM"
        model.addAttribute("salesData", list);
        model.addAttribute("month", yearMonth);

        return "/product/product-sales-main";
    }

    @GetMapping("/sales/search")
    public String salesByKeyword(
            @RequestParam(required = false) String prodTyCd,
            @RequestParam(required = false) String prodNm,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String dateType, // 날짜 조건 (판매시작일/종료일)
            @RequestParam(required = false) String searchBgnYmd, // 시작 날짜
            @RequestParam(required = false) String searchEndYmd, // 종료 날짜
            @RequestParam(defaultValue = "1") int page,
            Model model,
            HttpServletRequest request
    ){
        return "/product/product-sales-search";
    }
}
