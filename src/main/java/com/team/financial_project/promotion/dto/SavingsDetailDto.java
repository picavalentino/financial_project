package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsDetailDto {
    // 회차별 상세 정보
    private Integer installmentNo;      // 회차
    private BigDecimal installmentAmt;  // 회차불입금액
    private BigDecimal accumulatedAmt;  // 누적불입금액
    private BigDecimal installmentInt;  // 회차이자
    private BigDecimal installmentPrincipal; // 회차원리금
}
