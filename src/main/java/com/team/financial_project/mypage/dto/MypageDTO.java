package com.team.financial_project.mypage.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MypageDTO {

        // 유저 기본 정보
        private String userId;
        private String userJncmpYmd;
        private String userName;
        private String userTelno;
        private String userEmail;
        private String userImgpath;
        private String userDeptName; // 부서명
        private String userJobName;  // 직무명


        // 나의 상담 정보 리스트
        private List<InquireDTO> inquiries;

        // 나의 상품 정보 리스트
        private List<CustProdDTO> custProds;

}





