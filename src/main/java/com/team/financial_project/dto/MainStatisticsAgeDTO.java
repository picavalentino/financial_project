package com.team.financial_project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MainStatisticsAgeDTO {
    private int totalCount;
    private int age10sUnder;
    private int age20s;
    private int age30s;
    private int age40s;
    private int age50s;
    private int age60sUp;

}
