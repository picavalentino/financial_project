package com.team.financial_project.financingCalculate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateProductDTO {
    private String prod_code;
    private String prod_pay_ty_cd;
    private String prod_air_min;
    private String prod_air_max;
    private String prod_int_tax_ty_cd;
    private String prod_nm;
    private String prod_sbstg_ty_cd;
}
