package com.team.financial_project.mapper;

import com.team.financial_project.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    /* 상품관리 페이지 */
    List<ProductDTO> findAll();
    ProductDTO findById(Long prodSn);
    void updateProduct(ProductDTO dto);
    void deleteProduct(BigDecimal prodSn);
    void insertProduct(ProductDTO dto);
    int findCdSizeByName(@Param("keyword") String keyword);
    List<ProductDTO> searchProducts(Map<String, Object> searchParams);
    List<ProductDTO> getProductsSorted(String sortColumn, String sortDirection);

    /* 매출 조회 페이지 */
    List<Map<String, Object>> findSavingsSales(LocalDate startOfMonth, LocalDate endOfMonth);
    List<Map<String, Object>> findLoanSales(LocalDate startOfMonth, LocalDate endOfMonth);
    List<Map<String, Object>> findDepositSales(LocalDate startOfMonth, LocalDate endOfMonth);
    List<Map<String, Object>> findLumpSumSales(LocalDate startOfMonth, LocalDate endOfMonth);

    List<Map<String, Object>> rankedSavingsTop2(LocalDate startOfMonth, LocalDate endOfMonth);
    List<Map<String, Object>> rankedLoansTop2(LocalDate startOfMonth, LocalDate endOfMonth);
    List<Map<String, Object>> rankedDepositTop2(LocalDate startOfMonth, LocalDate endOfMonth);
    List<Map<String, Object>> rankedLumpSumTop2(LocalDate startOfMonth, LocalDate endOfMonth);

    List<Map<String, Object>> findSalesData(LocalDate startOfMonth, LocalDate endOfMonth, String userId, String dsgnDsTy, String prodNm);
    List<Map<String, Object>> rankedDepositTop2ByConditions(LocalDate startOfMonth, LocalDate endOfMonth, String userId, String prodNm);
    List<Map<String, Object>> rankedSavingsTop2ByConditions(LocalDate startOfMonth, LocalDate endOfMonth, String userId, String prodNm);
    List<Map<String, Object>> rankedLumpSumTop2ByConditions(LocalDate startOfMonth, LocalDate endOfMonth, String userId, String prodNm);
    List<Map<String, Object>> rankedLoansTop2ByConditions(LocalDate startOfMonth, LocalDate endOfMonth, String userId, String prodNm);
}
