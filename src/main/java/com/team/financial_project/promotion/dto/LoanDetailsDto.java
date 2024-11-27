package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetailsDto {
    private int installment;       // 회차
    private double monthlyInterest; // 월 이자
    private double principal;       // 원금
    private double totalPayment;    // 총 납입 금액
}
