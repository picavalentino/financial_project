package com.team.financial_project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MainInquireDTO {
    private Long inqId;
    private String userId;
    private String inqCategory;
    private String inqTitle;
    private Timestamp inqCreateAt;
}
