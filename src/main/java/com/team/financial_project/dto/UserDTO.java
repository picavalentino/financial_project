package com.team.financial_project.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String user_id;           // 사원 번호
    private String user_name;         // 직원 이름
    private String user_email;        // 직원 이메일
    private String user_telno;        // 직원 핸드폰번호
    private String user_jncmp_ymd;    // 직원 입사일자
    private String user_dept_cd;      // 직원 부서코드
    private String user_jbps_ty_cd;   // 직위 코드
    private String user_auth_cd;      // 직원 역할 코드
    private String user_status;       // 직원 현재 상태
    private String user_pw;           // 직원 비밀번호
    private String user_imgpath;      // 직원 이미지
    private String user_birthday;     // 직원 생년월일
    private String user_auth_cd_code_cl;   // 직원 역할코드 분류코드
    private String user_status_code_cl;    // 직원 현재상태 분류코드
    private String user_dept_cd_code_cl;   // 직원 부서 분류코드
    private String user_jbps_ty_cd_code_cl;  // 직위 분류코드
    private String dept_name; // 부서 이름
    private String position_name; // 직위 이름
}
