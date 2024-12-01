package com.team.financial_project.mapper;

import com.team.financial_project.dto.ProdHistDTO;
import com.team.financial_project.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<ProductDTO> findAllList();

    ProductDTO getProductById(@Param("prodSn") BigDecimal prodSn); // @Param 추가

    List<ProdHistDTO> getProdHistById(@Param("prodSn") BigDecimal prodSn); // @Param 추가

    void updateProduct(@Param("dto") ProductDTO dto, @Param("userId") String userId); // @Param 추가

    void deleteProduct(@Param("prodSn") BigDecimal prodSn); // @Param 추가

    void insertProduct(@Param("dto") ProductDTO dto); // @Param 추가

    int findCdSizeByName(@Param("keyword") String keyword); // @Param 추가

    List<ProductDTO> searchProducts(@Param("searchParams") Map<String, Object> searchParams); // @Param 추가

    List<ProductDTO> getProductsSorted(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection); // @Param 추가

    /* 매출 조회 페이지 */
    List<Map<String, Object>> findSavingsSales(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> findLoanSales(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> findDepositSales(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> findLumpSumSales(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> rankedSavingsTop2(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> rankedLoansTop2(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> rankedDepositTop2(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> rankedLumpSumTop2(@Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth); // @Param 추가

    List<Map<String, Object>> findSalesData(
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth,
            @Param("userId") String userId,
            @Param("dsgnDsTy") String dsgnDsTy,
            @Param("prodNm") String prodNm
    ); // @Param 추가

    List<Map<String, Object>> rankedDepositTop2ByConditions(
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth,
            @Param("userId") String userId,
            @Param("prodNm") String prodNm
    ); // @Param 추가

    List<Map<String, Object>> rankedSavingsTop2ByConditions(
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth,
            @Param("userId") String userId,
            @Param("prodNm") String prodNm
    ); // @Param 추가

    List<Map<String, Object>> rankedLumpSumTop2ByConditions(
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth,
            @Param("userId") String userId,
            @Param("prodNm") String prodNm
    ); // @Param 추가

    List<Map<String, Object>> rankedLoansTop2ByConditions(
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth,
            @Param("userId") String userId,
            @Param("prodNm") String prodNm
    ); // @Param 추가

    int updateProductStatus();
}
