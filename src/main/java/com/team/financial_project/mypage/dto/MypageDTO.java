package com.team.financial_project.mypage.dto;

import lombok.Data;

import java.util.List;

@Data
public class MypageDTO {

        // 유저 기본 정보
        private String userId;
        private String userIncmpYmd;
        private String userDeptCd;
        private String userDeptCdCodeCl;
        private String userJbpsTyCd;
        private String userJbpsTyCdCodeCl;
        private String userName;
        private String userTelno;
        private String userEmail;

        // 나의 상담 정보 리스트
        private List<InquireDTO> inquiries;

        // 나의 상품 정보 리스트
        private List<CustProdDTO> custProds;
    }





