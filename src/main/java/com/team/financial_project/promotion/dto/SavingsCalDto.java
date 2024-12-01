package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsCalDto {
    // 입력 필드
    private BigDecimal savgCircleAmt; // 불입금액
    private Integer savgGoalPrd;      // 목표기간
    private BigDecimal savgAplyRate; // 적용금리
    private BigDecimal prodIntTaxRate; // 이자과세율
    private LocalDate savgStrtDt;    // 시작일자

    // 결과 필드
    private BigDecimal savgTotDpstAmt; // 불입금액합계
    private BigDecimal savgTotDpstInt; // 세전이자
    private BigDecimal savgTotRcveAmt; // 세전수령액
    private BigDecimal savgIntTaxAmt;  // 이자과세금
    private BigDecimal savgAtxRcveAmt; // 세후수령액

    // 상세 데이터 리스트
    private List<SavingsDetailDto> detailList; // 회차별 상세 정보
}
