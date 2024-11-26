package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalDto {
    // 입력 필드
    private BigDecimal savgCircleAmt; // 대출금액
    private Integer savgGoalPrd;      // 대출기간
    private BigDecimal savgAplyRate; // 적용금리
    private LocalDate savgStrtDt;    // 시작일자

    // 결과 필드
    private BigDecimal savgTotDpstAmt; // 대출액
    private BigDecimal savgTotDpstInt; // 총이자
    private BigDecimal savgAtxRcveAmt; // 총납입금액

    // 상세 데이터 리스트
    private List<LoanDetailsDto> detailList; // 회차별 상세 정보
}
