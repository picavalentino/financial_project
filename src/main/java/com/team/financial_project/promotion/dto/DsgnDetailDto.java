package com.team.financial_project.promotion.dto;

import lombok.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DsgnDetailDto {
    private String effectiveDsgnDate; // 설계일자
    private Long prodSn; // 상품번호
    private String prodCd; // 상품코드
    private String prodNm; // 상품이름
    private String custNm; // 고객이름
    private String custRrn; // 고객주민번호
    private String custEmail; // 고객이메일
    private String custTelno; // 고객전화번호
    private String custAddr; // 고객주소
    private LocalDate custCreateAt; // 고객등록일자
    private String codeNm; // 고객 코드 이름
    private String userName; // 담당자이름
    private LocalDate savgStrtDt; // 시작일자
    private LocalDate savgMtrDt; // 목표일자
    private BigDecimal savgGoalAmt; // 불입금액
    private Integer savgGoalPrd; // 목표기간
    private BigDecimal savgAplyRate; // 적용이율
    private String savgIntTaxTyCd; // 과세기준 코드
    private String savgPayTyCd; // 납입주기 코드
    private String savgTaxRate; // 과세기준
    private String payTerm; // 납입주기
    private BigDecimal newSavgTaxRate; // 과세기준 타입변환

    // 포맷된 금액 반환
    public String getFormattedSavgGoalAmt() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(savgGoalAmt);
    }

    public BigDecimal getNewSavgTaxRate() {
        return new BigDecimal(this.savgTaxRate); // 변환된 데이터 반환
    }
}
