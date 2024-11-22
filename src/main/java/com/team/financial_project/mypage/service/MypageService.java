package com.team.financial_project.mypage.service;

import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.mypage.mapper.MypageMapper;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    private final MypageMapper mypageMapper;

    public MypageService(MypageMapper mypageMapper) {
        this.mypageMapper = mypageMapper;
    }

    public MypageDTO getMypageByUserId(String userId) {
        MypageDTO mypageDTO = mypageMapper.selectUserInfo(userId); // 유저 정보 가져오기
        mypageDTO.setInquiries(mypageMapper.selectInquiriesByUserId(userId)); // 상담 리스트
        mypageDTO.setCustProds(mypageMapper.selectCustProdsByUserId(userId)); // 가입 상품 리스트
        return mypageDTO;
    }

}
