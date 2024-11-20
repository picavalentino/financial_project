package com.team.financial_project.financingCalculate.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MonthlyInterestDTO {
    private int installment;       // 회차
    private LocalDate dueDate;     // 납입일
    private double monthlyInterest; // 월 이자
    private double principal;       // 원금
    private double totalPayment;    // 총 납입 금액

    public MonthlyInterestDTO(int installment, LocalDate dueDate, double monthlyInterest) {
        this(installment, dueDate, monthlyInterest, 0, monthlyInterest); // 기본값 설정
    }
}
