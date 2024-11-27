package com.team.financial_project.counsel.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TbCounselDTO {
    private String cust_id;      // 고객 번호
    private String user_id;      // 사원번호
    private String counsel_category; // 상담 내용 카테고리
    private String counsel_category_nm; // 상담 내용 카테고리명
    private String counsel_content;  // 상담 내용 상세
    private LocalDateTime counsel_create_at;  // 작성일시
    private LocalDateTime counsel_update_at;  // 수정일시
    private String counsel_stcd;     // 상담 내용 상태 코드
    private Long counsel_id;     // 상담 인덱스
    private String counsel_stcd_code_cl;   // 상담내용상태코드분류 : DEFAULT 100
    private String counsel_category_code_cl;   // 상담내용 카테고리 코드분류 : DEFAULT 700
    private String user_dept_cd;  // 부서코드 코드분류 : DEFAULT 200
    private String user_dept_nm; // 부서명
    private String user_name;   // 사원이름
}
