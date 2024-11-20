package com.team.financial_project.financingCalculate.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanDTO {
    private double loanTotal;
    private int loanPeriod;
    private double loanInterestRate;
}
