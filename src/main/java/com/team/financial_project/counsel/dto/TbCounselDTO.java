package com.team.financial_project.counsel.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TbCounselDTO {
    private String custId;      // 고객 번호
    private String userId;      // 사원번호
    private String counselCategory; // 상담 내용 카테고리
    private String counselContent;  // 상담 내용 상세
    private LocalDateTime counselCreateAt;  // 작성일시
    private LocalDateTime counselUpdateAt;  // 수정일시
    private String counselStcd;     // 상담 내용 상태 코드
    private Long counselId;     // 상담 인덱스
    private String counselStcdCodeCl;   // 상담내용상태코드분류 : DEFAULT 100
    private String counselCategoryCodeCl;   // 상담내용 카테고리 코드분류 : DEFAULT 400
}
