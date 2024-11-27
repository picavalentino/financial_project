package com.team.financial_project.promotion.service;

import com.team.financial_project.promotion.dto.ProductInfoDto;
import com.team.financial_project.promotion.mapper.PromoCalMapper;
import com.team.financial_project.promotion.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoCalService {
    @Autowired
    PromoCalMapper promoCalMapper;
    public String findDsTyCd(Long dsgnSn) {
        return promoCalMapper.findDsTyCd(dsgnSn);
    }

    public List<ProductInfoDto> getAcmlProductList(String prodCd, String prodNm) {
        return promoCalMapper.getAcmlProductList();
    }

    public List<ProductInfoDto> getDpstProductList(String prodCd, String prodNm) {
        return promoCalMapper.getDpstProductList();
    }

    public List<ProductInfoDto> getLoanProductList(String prodCd, String prodNm) {
        return promoCalMapper.getLoanProductList();
    }
}
