package com.team.financial_project.financingCalculate.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanResultDTO {
    private double totalInterest;         // 총 이자
    private double totalRepayment;        // 만기 상환 총액
    private List<MonthlyInterestDTO> interestDetails; // 회차별 상세 정보

}
