package com.team.financial_project.financingCalculate.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CalculateResultDTO {
    private String totalMoney = "0";
    private String preTaxInterest = "0";
    private String preTaxMyMoney = "0";
    private String tax = "0";
    private String finalMoney = "0";
}
