package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    // 고객 아이디
    public String custId;
    // 고객명
    public String custNm;
    // 생년월일
    public String custBirth;
    // 이메일
    public String custEmail;
    // 전화번호
    public String custTelno;
    // 직업
    public String custOccp;
    // 주소
    public String custAddr;
    // 작성일자
    public String custCreateAt;
    // 담당자 아이디
    public String userId;
    // 담당자명
    public String userName;
}
