package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositCalDto {
    private BigDecimal depositAmt; // 예치금액
    private int goalPeriod; // 목표기간 (개월)
    private BigDecimal aplyRate; // 적용금리 (%)
    private BigDecimal taxRate; // 이자 과세율 (%)
    private LocalDate startDate; // 예금 시작일자

    private BigDecimal totalDepositAmt; // 예치금
    private BigDecimal totalInterest; // 세전 이자
    private BigDecimal totalPreTax; // 세전 수령액
    private BigDecimal taxAmount; // 이자 과세금
    private BigDecimal totalAfterTax; // 세후 수령액

    private DepositDetailDto detail; // 상세 계산 정보
}
