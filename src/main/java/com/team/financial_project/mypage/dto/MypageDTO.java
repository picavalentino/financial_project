package com.team.financial_project.mypage.dto;

import lombok.Data;

@Data
public class MypageDTO {
    //사원번호
    private String user_id;
    //입사일
    private String user_incmp_ymd;
    //부서
    private String user_dept_cd;
    private String user_dept_cd_code_cl;
    //직무
    private String user_jbps_ty_cd;
    private String user_jbps_ty_cd_code_cl;
    //이름
    private String user_name;
    //휴대전화
    private String user_telno;
    //email
    private String user_email;



    //고객이름
    private String cust_nm;
    //가입상품
    private String  dsgn_ds_ty_cd;
    private String  dsgn_ds_ty_cd_code_cl;
    //가입상태 여부
    private String  dsgn_ds_state_cd;
    private String  dsgn_ds_state_cd_code_cl;
    // 고객 이메일
    private String cust_email;

    //작성글 제목
    private String inq_title;
    //글 작성일
    private String inq_create_at;
}
