package com.team.financial_project.product.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long prodSn;                 // 상품 일련번호
    private String prodNm;                 // 상품명
    private Long prodInstlAmtMin;    // 최소 가입한도
    private Long prodInstlAmtMax;    // 최대 가입한도
    private LocalDateTime prodCreateAt;    // 생성 일시
    private LocalDateTime prodUpdateAt;    // 수정 일시
    private String userId;                 // 담당직원 사원번호
    private String prodCd;                 // 상품 코드
    private String prodTyCd;               // 상품 유형 코드
    private String prodSbstgTyCd;          // 가입대상코드
    private String prodPayTyCd;            // 납입주기코드
    private BigDecimal prodAirMin;         // 최소 이자율
    private BigDecimal prodAirMax;         // 최대 이자율
    private String prodAirBgnYmd;       // 이자율 시작일
    private String prodAirEndYmd;       // 이자율 종료일
    private String prodIntTaxTyCd;         // 세금 유형 코드
    private String prodCurrStcd;           // 현재 상태 코드
    private String prodNtslBgnYmd;      // 판매 시작일
    private String prodNtslEndYmd;      // 판매 종료일
}
