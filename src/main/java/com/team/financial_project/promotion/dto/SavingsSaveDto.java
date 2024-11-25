package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsSaveDto {
    // TB_CUSTPROD_DSGN 관련 필드
    private Integer prodSn;
    private String custId;
    private String userId;
    private String dsgnCreateAt;
    private String dsgnUpdateAt;
    private String prodDsTyCd;
    private String dsgnPrgStcd;

    // TB_PROD_DSGN_SAVG 관련 필드
    private String prodPayTyCd;
    private Integer savgGoalPrd;
    private String savgStrtDt;
    private String savgMtrDt;
    private String savgAplyRate;
    private String savgIntTaxTyCd;
    private String savgCircleAmt;
    private String savgTotDpstAmt;
    private String savgTotDpstInt;
    private String savgIntTaxAmt;
    private String savgAtxRcveAmt;
}
