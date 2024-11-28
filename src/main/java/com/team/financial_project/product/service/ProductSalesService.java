package com.team.financial_project.product.service;

import com.team.financial_project.dto.TotalSalesDTO;
import com.team.financial_project.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
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

        // 날짜 문자열로 변환 (MyBatis에서 사용할 수 있도록)
//        String startDate = startOfMonth.toString();
//        String endDate = endOfMonth.toString();

        // 상품 유형별 매출 조회 (예금, 적금, 목돈, 대출 순서)
        Map<String, List<Map<String, Object>>> salesDataMap = Map.of(
                "예금", productMapper.rankedDepositTop2(startOfMonth, endOfMonth),
                "적금", productMapper.rankedSavingsTop2(startOfMonth, endOfMonth),
                "목돈", productMapper.rankedLumpSumTop2(startOfMonth, endOfMonth),
                "대출", productMapper.rankedLoansTop2(startOfMonth, endOfMonth)
        );

        // 상품 유형별 데이터를 순회하면서 처리
        for (Map.Entry<String, List<Map<String, Object>>> entry : salesDataMap.entrySet()) {
            String dsgnDsTy = entry.getKey();
            List<Map<String, Object>> rankedTop2 = entry.getValue();

            TotalSalesDTO dto = new TotalSalesDTO();
            dto.setDsgnDsTy(dsgnDsTy);

            // 매출 데이터 처리
            List<Map<String, Object>> salesData = switch (dsgnDsTy) {
                case "예금" -> productMapper.findDepositSales(startOfMonth, endOfMonth);
                case "적금" -> productMapper.findSavingsSales(startOfMonth, endOfMonth);
                case "목돈" -> productMapper.findLumpSumSales(startOfMonth, endOfMonth);
                case "대출" -> productMapper.findLoanSales(startOfMonth, endOfMonth);
                default -> List.of();
            };

            if (!salesData.isEmpty()) {
                Map<String, Object> data = salesData.get(0);
                dto.setTotalCounts(Long.valueOf(String.valueOf(data.get("total_design_count"))));
                dto.setTotalAmounts(Long.valueOf(String.valueOf(data.get("total_sales_amount"))));
                dto.setFormattedTotalAmounts(decimalFormat.format(dto.getTotalAmounts()));
            }

            // 상위 2개 데이터 처리
            List<String> countsTopList = rankedTop2.stream()
                    .map(row -> row.get("product_name") + " (" + row.get("total_design_count") + ")")
                    .toList();

            List<String> amountsTopList = rankedTop2.stream()
                    .map(row -> row.get("product_name") + " (" + decimalFormat.format(row.get("total_deposit_amount")) + ")")
                    .toList();

            dto.setCountsTop2(countsTopList);
            dto.setAmountsTop2(amountsTopList);

            totalSalesList.add(dto);
        }

        return totalSalesList;
    }

    public List<TotalSalesDTO> findSalesByConditions(
            LocalDate startOfMonth, LocalDate endOfMonth, String userId, String dsgnDsTy, String prodNm
    ) {
        List<TotalSalesDTO> totalSalesList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        // 기본 매출 데이터 매퍼 호출
        List<Map<String, Object>> salesData = productMapper.findSalesData(startOfMonth, endOfMonth, userId, dsgnDsTy, prodNm);

        // 상품 유형별 매출 조회 (예금, 적금, 목돈, 대출 순서)
        Map<String, List<Map<String, Object>>> salesDataMap = Map.of(
                "예금", productMapper.rankedDepositTop2ByConditions(startOfMonth, endOfMonth, userId, prodNm),
                "적금", productMapper.rankedSavingsTop2ByConditions(startOfMonth, endOfMonth, userId, prodNm),
                "목돈", productMapper.rankedLumpSumTop2ByConditions(startOfMonth, endOfMonth, userId, prodNm),
                "대출", productMapper.rankedLoansTop2ByConditions(startOfMonth, endOfMonth, userId, prodNm)
        );

        // 상품 유형별 데이터를 순회하면서 처리
        for (Map.Entry<String, List<Map<String, Object>>> entry : salesDataMap.entrySet()) {
            String category = entry.getKey(); // 예금, 적금 등
            List<Map<String, Object>> rankedTop2 = entry.getValue();

            TotalSalesDTO dto = new TotalSalesDTO();
            dto.setDsgnDsTy(category);

            // 해당 유형에 맞는 매출 데이터 처리
            List<Map<String, Object>> filteredData = salesData.stream()
                    .filter(data -> data.get("dsgn_ds_ty").equals(category))
                    .toList();

            if (!filteredData.isEmpty()) {
                Map<String, Object> data = filteredData.get(0);
                dto.setTotalCounts(Long.valueOf(String.valueOf(data.get("total_design_count"))));
                dto.setTotalAmounts(Long.valueOf(String.valueOf(data.get("total_sales_amount"))));
                dto.setFormattedTotalAmounts(decimalFormat.format(dto.getTotalAmounts()));
            }

            // 상위 2개 데이터 처리
            List<String> countsTopList = rankedTop2.stream()
                    .map(row -> row.get("product_name") + " (" + row.get("total_design_count") + ")")
                    .toList();

            List<String> amountsTopList = rankedTop2.stream()
                    .map(row -> row.get("product_name") + " (" + decimalFormat.format(row.get("total_deposit_amount")) + ")")
                    .toList();

            dto.setCountsTop2(countsTopList);
            dto.setAmountsTop2(amountsTopList);

            totalSalesList.add(dto);
        }

        return totalSalesList;
    }

}
