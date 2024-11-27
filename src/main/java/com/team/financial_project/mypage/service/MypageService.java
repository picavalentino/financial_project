package com.team.financial_project.mypage.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.main.service.S3Service;
import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.mypage.mapper.MypageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final S3Service s3Service;

    public MypageService(MypageMapper mypageMapper, S3Service s3Service) {
        this.mypageMapper = mypageMapper;
        this.s3Service = s3Service;
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



}
