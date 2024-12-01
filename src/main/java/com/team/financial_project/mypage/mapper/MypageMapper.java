package com.team.financial_project.mypage.mapper;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mypage.dto.CustProdDTO;
import com.team.financial_project.mypage.dto.InquireDTO;
import com.team.financial_project.mypage.dto.MypageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MypageMapper {
    MypageDTO selectUserInfo(String userId);// 유저 정보
    List<InquireDTO> selectInquiriesByUserId(String userId); // 상담 리스트
    List<CustProdDTO> selectCustProdsByUserId(String userId); // 가입 상품 리스트

    int updateUser(MypageDTO mypageDTO);
    int updateUserInfo(MypageDTO mypageDTO);
    int updatePassword(@Param("userId") String userId, @Param("encryptedPassword") String encryptedPassword);


    UserDTO certifyByUserTelno(String telno);
}