package com.team.financial_project.promotion.mapper;

import com.team.financial_project.promotion.dto.ProductInfoDto;
import com.team.financial_project.promotion.dto.SavingsSaveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromoCalMapper {
    // 설계번호 찾기
    String findDsTyCd(Long dsgnSn);

    // 목돈적금 상품 찾기
    List<ProductInfoDto> getAcmlProductList();

    // 예금 상품 찾기
    List<ProductInfoDto> getDpstProductList();

    // 대출 상품 찾기
    List<ProductInfoDto> getLoanProductList();

    // 목돈 적금 설계 저
    void insertCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void insertProdDsgnacml(SavingsSaveDto savingsSaveDto);
}
