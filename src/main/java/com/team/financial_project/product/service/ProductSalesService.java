package com.team.financial_project.product.service;

import com.team.financial_project.dto.ProductSalesDTO;
import com.team.financial_project.dto.TotalSalesDTO;
import com.team.financial_project.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductSalesService {
    private final ProductMapper productMapper;

    public ProductSalesService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<TotalSalesDTO> thisMonthSales(LocalDate startOfMonth, LocalDate endOfMonth) {
        List<TotalSalesDTO> totalSalesList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        // 1. 적금 매출 조회
        List<Map<String, Object>> savingsSalesData = productMapper.thisMonthSavingsSales(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedSavingsCountsTop2 = productMapper.rankedSavingsCountsTop2(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedSavingsAmountsTop2 = productMapper.rankedSavingsAmountsTop2(startOfMonth, endOfMonth);

        TotalSalesDTO savingsDTO = new TotalSalesDTO();
        if (!savingsSalesData.isEmpty()) {
            Map<String, Object> salesData = savingsSalesData.get(0);
            savingsDTO.setProdTyCd("적금"); // 상품유형
            savingsDTO.setTotalCounts(Long.valueOf(String.valueOf(salesData.get("total_design_count"))));
            savingsDTO.setTotalAmounts(Long.valueOf(String.valueOf(salesData.get("total_sales_amount"))));
            savingsDTO.setFormattedTotalAmounts(decimalFormat.format(savingsDTO.getTotalAmounts()));
        }

// 상위 2개 데이터 설정
        List<String> savingsCountsTop2 = new ArrayList<>();
        List<String> savingsAmountsTop2 = new ArrayList<>();

// 설계 건수 기준 상위 2개 상품
        for (Map<String, Object> row : rankedSavingsCountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDesignCount = Long.valueOf(String.valueOf(row.get("total_design_count")));
            savingsCountsTop2.add(productName + " (" + totalDesignCount + ")");
        }

// 설계 금액 기준 상위 2개 상품
        for (Map<String, Object> row : rankedSavingsAmountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDepositAmount = Long.valueOf(String.valueOf(row.get("total_deposit_amount")));
            savingsAmountsTop2.add(productName + " (" + decimalFormat.format(totalDepositAmount) + ")");
        }

        savingsDTO.setCountsTop2(savingsCountsTop2);
        savingsDTO.setAmountsTop2(savingsAmountsTop2);
        totalSalesList.add(savingsDTO);

        // 2. 대출 매출 조회
        List<Map<String, Object>> loanSalesData = productMapper.thisMonthLoanSales(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedLoansCountsTop2 = productMapper.rankedLoansCountsTop2(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedLoansAmountsTop2 = productMapper.rankedLoansAmountsTop2(startOfMonth, endOfMonth);

        TotalSalesDTO loanDTO = new TotalSalesDTO();
        if (!loanSalesData.isEmpty()) {
            Map<String, Object> salesData = loanSalesData.get(0);
            loanDTO.setProdTyCd("대출"); // 상품유형
            loanDTO.setTotalCounts(Long.valueOf(String.valueOf(salesData.get("total_design_count"))));
            loanDTO.setTotalAmounts(Long.valueOf(String.valueOf(salesData.get("total_sales_amount"))));
            loanDTO.setFormattedTotalAmounts(decimalFormat.format(loanDTO.getTotalAmounts()));
        }

// 상위 2개 데이터 설정
        List<String> loanCountsTop2 = new ArrayList<>();
        List<String> loanAmountsTop2 = new ArrayList<>();

// 설계 건수 기준 상위 2개 상품
        for (Map<String, Object> row : rankedLoansCountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDesignCount = Long.valueOf(String.valueOf(row.get("total_design_count")));
            loanCountsTop2.add(productName + " (" + totalDesignCount + ")");
        }

// 설계 금액 기준 상위 2개 상품
        for (Map<String, Object> row : rankedLoansAmountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDepositAmount = Long.valueOf(String.valueOf(row.get("total_deposit_amount")));
            loanAmountsTop2.add(productName + " (" + decimalFormat.format(totalDepositAmount) + ")");
        }

        loanDTO.setCountsTop2(loanCountsTop2);
        loanDTO.setAmountsTop2(loanAmountsTop2);
        totalSalesList.add(loanDTO);

// 3. 예금 매출 조회
        List<Map<String, Object>> depositSalesData = productMapper.thisMonthDepositSales(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedDepositCountsTop2 = productMapper.rankedDepositCountsTop2(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedDepositAmountsTop2 = productMapper.rankedDepositAmountsTop2(startOfMonth, endOfMonth);

        TotalSalesDTO depositDTO = new TotalSalesDTO();
        if (!depositSalesData.isEmpty()) {
            Map<String, Object> salesData = depositSalesData.get(0);
            depositDTO.setProdTyCd("예금"); // 상품유형
            depositDTO.setTotalCounts(Long.valueOf(String.valueOf(salesData.get("total_design_count"))));
            depositDTO.setTotalAmounts(Long.valueOf(String.valueOf(salesData.get("total_sales_amount"))));
            depositDTO.setFormattedTotalAmounts(decimalFormat.format(depositDTO.getTotalAmounts()));
        }

// 상위 2개 데이터 설정
        List<String> depositCountsTop2 = new ArrayList<>();
        List<String> depositAmountsTop2 = new ArrayList<>();

// 설계 건수 기준 상위 2개 상품
        for (Map<String, Object> row : rankedDepositCountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDesignCount = Long.valueOf(String.valueOf(row.get("total_design_count")));
            depositCountsTop2.add(productName + " (" + totalDesignCount + ")");
        }

// 설계 금액 기준 상위 2개 상품
        for (Map<String, Object> row : rankedDepositAmountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDepositAmount = Long.valueOf(String.valueOf(row.get("total_deposit_amount")));
            depositAmountsTop2.add(productName + " (" + decimalFormat.format(totalDepositAmount) + ")");
        }

        depositDTO.setCountsTop2(depositCountsTop2);
        depositDTO.setAmountsTop2(depositAmountsTop2);
        totalSalesList.add(depositDTO);

// 4. 목돈 매출 조회
        List<Map<String, Object>> lumpSumSalesData = productMapper.thisMonthLumpSumSales(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedLumpSumCountsTop2 = productMapper.rankedLumpSumCountsTop2(startOfMonth, endOfMonth);
        List<Map<String, Object>> rankedLumpSumAmountsTop2 = productMapper.rankedLumpSumAmountsTop2(startOfMonth, endOfMonth);

        TotalSalesDTO lumpSumDTO = new TotalSalesDTO();
        if (!lumpSumSalesData.isEmpty()) {
            Map<String, Object> salesData = lumpSumSalesData.get(0);
            lumpSumDTO.setProdTyCd("목돈"); // 상품유형
            lumpSumDTO.setTotalCounts(Long.valueOf(String.valueOf(salesData.get("total_design_count"))));
            lumpSumDTO.setTotalAmounts(Long.valueOf(String.valueOf(salesData.get("total_sales_amount"))));
            lumpSumDTO.setFormattedTotalAmounts(decimalFormat.format(lumpSumDTO.getTotalAmounts()));
        }

// 상위 2개 데이터 설정
        List<String> lumpSumCountsTop2 = new ArrayList<>();
        List<String> lumpSumAmountsTop2 = new ArrayList<>();

// 설계 건수 기준 상위 2개 상품
        for (Map<String, Object> row : rankedLumpSumCountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDesignCount = Long.valueOf(String.valueOf(row.get("total_design_count")));
            lumpSumCountsTop2.add(productName + " (" + totalDesignCount + ")");
        }

// 설계 금액 기준 상위 2개 상품
        for (Map<String, Object> row : rankedLumpSumAmountsTop2) {
            String productName = (String) row.get("product_name");
            Long totalDepositAmount = Long.valueOf(String.valueOf(row.get("total_deposit_amount")));
            lumpSumAmountsTop2.add(productName + " (" + decimalFormat.format(totalDepositAmount) + ")");
        }

        lumpSumDTO.setCountsTop2(lumpSumCountsTop2);
        lumpSumDTO.setAmountsTop2(lumpSumAmountsTop2);
        totalSalesList.add(lumpSumDTO);

        return totalSalesList;
    }

}
