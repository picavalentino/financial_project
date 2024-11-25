package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalSalesDTO {
    private String prodTyCd;
    private Long totalCounts;
    private Long totalAmounts;
    private List<String> countsTop2;
    private List<String> amountsTop2;
    private String formattedTotalAmounts;
}
