package com.team.financial_project.schedule.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
@Data
@Getter
@Setter
public class CustomerMtrDtDTO {
    private String cust_nm;
    private Date dpst_mtr_dt;
    private int dsgn_sn;
    private String dsgn_ds_ty_cd;
}
