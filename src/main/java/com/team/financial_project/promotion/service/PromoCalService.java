package com.team.financial_project.promotion.service;

import com.team.financial_project.promotion.mapper.PromoCalMapper;
import com.team.financial_project.promotion.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoCalService {
    @Autowired
    PromoCalMapper promoCalMapper;
    public String findDsTyCd(Long dsgnSn) {
        return promoCalMapper.findDsTyCd(dsgnSn);
    }
}
