package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositDetailDto {
    private BigDecimal principal; // 원금
    private BigDecimal interest; // 세전 이자
    private BigDecimal taxAmount; // 이자 과세금
    private BigDecimal afterTaxAmount; // 세후 수령액
}
