package com.team.financial_project.financingCalculate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalculateSavgDTO {
    private Long inputMoney;
    private Long period;
    private Double interest;
    private Double taxRate;
}

