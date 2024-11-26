package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsSaveDto {
    private Integer dsgnSn; // 자동 생성될 DSGN_SN 값

    // TB_CUSTPROD_DSGN 관련 필드
    private Integer prodSn;
    private String custId;
    private String userId;
    private LocalDate dsgnCreateAt; // LocalDate로 변경
    private LocalDate dsgnUpdateAt; // LocalDate로 변경
    private String prodDsTyCd;
    private String dsgnPrgStcd;

    // TB_PROD_DSGN_SAVG 관련 필드
    private Integer repaymentMethodCode;
    private String prodPayTyCd;
    private Integer savgGoalPrd;
    private LocalDate savgStrtDt;
    private LocalDate savgMtrDt;
    private BigDecimal savgAplyRate;
    private String savgIntTaxTyCd;
    private BigDecimal savgCircleAmt;
    private BigDecimal savgTotDpstAmt;
    private BigDecimal savgTotDpstInt;
    private BigDecimal savgIntTaxAmt;
    private BigDecimal savgAtxRcveAmt;
}
