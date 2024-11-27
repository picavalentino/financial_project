package com.team.financial_project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MainStatisticsSalesDTO {
    private int now;
    private int previousMonth1;
    private int previousMonth2;
    private int previousMonth3;
    private int previousMonth4;
}
