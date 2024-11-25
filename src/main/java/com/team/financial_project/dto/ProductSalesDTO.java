package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesDTO {
    // 매출 조회에 사용할 dto
    private String prodNm;
    private String prodTyCd;
    private String dsgnCounts;  // 설계건수
    private String dsgnAmounts;  // 설계금액
}
