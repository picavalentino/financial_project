package com.team.financial_project.promotion.mapper;

import com.team.financial_project.promotion.dto.DsgnDetailDto;
import com.team.financial_project.promotion.dto.ProductInfoDto;
import com.team.financial_project.promotion.dto.SavingsSaveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromoCalMapper {
    // 설계번호 찾기
    String findDsTyCd(Long dsgnSn);

    // 목돈적금 상품 찾기
    List<ProductInfoDto> getAcmlProductList(@Param("prodCd") String prodCd, @Param("prodNm") String prodNm);

    // 예금 상품 찾기
    List<ProductInfoDto> getDpstProductList(@Param("prodCd") String prodCd, @Param("prodNm") String prodNm);

    // 대출 상품 찾기
    List<ProductInfoDto> getLoanProductList(@Param("prodCd") String prodCd, @Param("prodNm") String prodNm);

    // 목돈 적금 설계 저장
    void insertCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void insertProdDsgnacml(SavingsSaveDto savingsSaveDto);

    // 예금 설계 저장
    void insertDpstCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void insertProdDsgnDpst(SavingsSaveDto savingsSaveDto);

    // 대출 설계 저장
    void insertLoanCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void insertProdDsgnLoan(SavingsSaveDto savingsSaveDto);

    // 상세페이지 출력
    DsgnDetailDto findSavgDetails(Long dsgnSn);

    DsgnDetailDto findAcmlDetails(Long dsgnSn);

    DsgnDetailDto findDpstDetails(Long dsgnSn);

    DsgnDetailDto findLoanDetails(Long dsgnSn);

    // 목돈 적금 업데이트
    void updateCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void updateProdDsgnAcml(SavingsSaveDto savingsSaveDto);

    // 예금 업데이트
    void updateDpstCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void updateProdDsgnDpst(SavingsSaveDto savingsSaveDto);

    // 적금 업데이트
    void updateSavgCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void updateProdDsgnSavg(SavingsSaveDto savingsSaveDto);

    // 대출 업데이트
    void updateLoanCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void updateProdDsgnLoan(SavingsSaveDto savingsSaveDto);
}
