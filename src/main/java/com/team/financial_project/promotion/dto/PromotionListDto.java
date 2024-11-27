package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionListDto {
    private String dsgnSn; // 설계고유번호 (pk)
    private String prodSn; // 상품설계번호
    private String custId; // 고객 id
    private String custNm; // 고객명
    private String userId; // 직원 id
    private String userNm; // 담당자 이름
    private String prodNm; // 상품명
    private String dsTyCd; // 설계유형
    private String mtrDate; // 만기일자
    private String prgStcd; // 진행상태
    private String mtrAmt; // 만기금액(잔액)

    private BigDecimal baseAmount; // 설계 금액
    private BigDecimal interestRate; // 금리
    private int period; // 기간 (개월)
    private LocalDate startDate; // 설계 시작일
}
