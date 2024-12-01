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
public class AutoMaturityDateDto {
    private Long dsgn_sn;
    private LocalDate acml_mtr_dt;
    private LocalDate dpst_mtr_dt;
    private LocalDate loan_mtr_dt;
    private LocalDate savg_mtr_dt;
}
