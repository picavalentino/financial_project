package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquireDTO {
    private BigDecimal inqId;
    private String userId;
    private String inqTitle;
    private String inqCategory;
    private String inqAnonym;
    private String inqContent;
    private String inqPwd;
    private String inqReply;
    private String inqAttachFile1;
    private String inqAttachFile2;
    private String inqAttachFile3;
    private String inqAttachFile4;
    private String inqAttachFile5;
    private String inqStatus;
    private LocalDateTime inqCreateAt;
    private LocalDateTime inqUpdateAt;
}
