package com.team.financial_project.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdHistDTO {
    private BigDecimal histId;
    private String userId;
    private BigDecimal prodSn;
    private BigDecimal prodAirMin;         // 최소 이자율
    private BigDecimal prodAirMax;         // 최대 이자율
    private String prodAirBgnYmd;       // 이자율 시작일
    private String prodAirEndYmd;       // 이자율 종료일
    private LocalDateTime histCreateAt;
    private String formattedHistCreateAt; // 포맷된 날짜
    private String histCurrStcd;
}
