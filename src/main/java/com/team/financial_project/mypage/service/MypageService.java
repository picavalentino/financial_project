package com.team.financial_project.mypage.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.main.service.S3Service;
import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.mypage.mapper.MypageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class MypageService {
    private final MypageMapper mypageMapper;
    private final BCryptPasswordEncoder pwEncoder;

    public MypageService(MypageMapper mypageMapper, BCryptPasswordEncoder pwEncoder) {
        this.mypageMapper = mypageMapper;
        this.pwEncoder = pwEncoder;
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
        try {
            int rowsAffected = mypageMapper.updateUserInfo(mypageDTO);
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateUserPassword(String userId, String plainPassword) {
        try {
            // 비밀번호 암호화
            String encryptedPassword = pwEncoder.encode(plainPassword);

            // MyBatis Mapper를 사용하여 업데이트 실행
            int rowsAffected = mypageMapper.updatePassword(userId, encryptedPassword);
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}