package com.team.financial_project.promotion.service;

import com.team.financial_project.promotion.dto.DsgnDetailDto;
import com.team.financial_project.promotion.dto.ProductInfoDto;
import com.team.financial_project.promotion.dto.SavingsSaveDto;
import com.team.financial_project.promotion.mapper.PromoCalMapper;
import com.team.financial_project.promotion.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromoCalService {
    @Autowired
    PromoCalMapper promoCalMapper;
    public String findDsTyCd(Long dsgnSn) {
        return promoCalMapper.findDsTyCd(dsgnSn);
    }

    public List<ProductInfoDto> getAcmlProductList(String prodCd, String prodNm) {
        return promoCalMapper.getAcmlProductList(prodCd, prodNm);
    }

    public List<ProductInfoDto> getDpstProductList(String prodCd, String prodNm) {
        return promoCalMapper.getDpstProductList(prodCd, prodNm);
    }

    public List<ProductInfoDto> getLoanProductList(String prodCd, String prodNm) {
        return promoCalMapper.getLoanProductList(prodCd, prodNm);
    }

    @Transactional
    public void save(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 삽입 (dsgnSn 값 자동 생성)
        promoCalMapper.insertCustprodDsgn(savingsSaveDto);

        // Step 2: 생성된 dsgnSn 값을 savingsSaveDto에 설정
        Integer generatedDsgnSn = savingsSaveDto.getDsgnSn();
        savingsSaveDto.setDsgnSn(generatedDsgnSn);

        // Step 3: TB_PROD_DSGN_ACML에 데이터 삽입
        promoCalMapper.insertProdDsgnacml(savingsSaveDto);
    }

    public void saveDpst(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 삽입 (dsgnSn 값 자동 생성)
        promoCalMapper.insertDpstCustprodDsgn(savingsSaveDto);

        // Step 2: 생성된 dsgnSn 값을 savingsSaveDto에 설정
        Integer generatedDsgnSn = savingsSaveDto.getDsgnSn();
        savingsSaveDto.setDsgnSn(generatedDsgnSn);

        // Step 3: TB_PROD_DSGN_APST에 데이터 삽입
        promoCalMapper.insertProdDsgnDpst(savingsSaveDto);
    }

    public void saveLoan(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 삽입 (dsgnSn 값 자동 생성)
        promoCalMapper.insertLoanCustprodDsgn(savingsSaveDto);

        // Step 2: 생성된 dsgnSn 값을 savingsSaveDto에 설정
        Integer generatedDsgnSn = savingsSaveDto.getDsgnSn();
        savingsSaveDto.setDsgnSn(generatedDsgnSn);

        // Step 3: TB_PROD_DSGN_APST에 데이터 삽입
        promoCalMapper.insertProdDsgnLoan(savingsSaveDto);
    }

    @Transactional
    public DsgnDetailDto findSavgDetails(Long dsgnSn) {
        return promoCalMapper.findSavgDetails(dsgnSn);
    }

    public DsgnDetailDto findAcmlDetails(Long dsgnSn) {
        return promoCalMapper.findAcmlDetails(dsgnSn);
    }

    public DsgnDetailDto findDpstDetails(Long dsgnSn) {
        return promoCalMapper.findDpstDetails(dsgnSn);
    }

    public DsgnDetailDto findLoanDetails(Long dsgnSn) {
        return promoCalMapper.findLoanDetails(dsgnSn);
    }

    @Transactional
    public void updateAcml(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 업데이트
        promoCalMapper.updateCustprodDsgn(savingsSaveDto);

        // Step 2: TB_PROD_DSGN_ACML에 데이터 업데이트
        promoCalMapper.updateProdDsgnAcml(savingsSaveDto);
    }

    public void updateDpst(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 업데이트
        promoCalMapper.updateDpstCustprodDsgn(savingsSaveDto);

        // Step 2: TB_PROD_DSGN_ACML에 데이터 업데이트
        promoCalMapper.updateProdDsgnDpst(savingsSaveDto);
    }

    public void updateSavg(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 업데이트
        promoCalMapper.updateSavgCustprodDsgn(savingsSaveDto);

        // Step 2: TB_PROD_DSGN_ACML에 데이터 업데이트
        promoCalMapper.updateProdDsgnSavg(savingsSaveDto);
    }

    public void updateLoan(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 업데이트
        promoCalMapper.updateLoanCustprodDsgn(savingsSaveDto);

        // Step 2: TB_PROD_DSGN_ACML에 데이터 업데이트
        promoCalMapper.updateProdDsgnLoan(savingsSaveDto);
    }
}
