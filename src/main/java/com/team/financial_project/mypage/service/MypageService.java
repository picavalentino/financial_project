package com.team.financial_project.mypage.service;

import com.team.financial_project.dto.UserDTO;
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
        // 기본 사용자 정보 조회
        MypageDTO mypageDTO = mypageMapper.selectUserInfo(userId);

        if (mypageDTO != null) {
            // 고객 정보 리스트 추가
            mypageDTO.setCustProds(mypageMapper.selectCustProdsByUserId(userId));

            // 작성글 리스트 추가
            mypageDTO.setInquiries(mypageMapper.selectInquiriesByUserId(userId));
        }

        return mypageDTO;
    }



    public boolean updateUserInfo(MypageDTO mypageDTO) {
        int rowsAffected = mypageMapper.updateUser(mypageDTO);
        return rowsAffected > 0; // 업데이트 성공 여부
    }


}
