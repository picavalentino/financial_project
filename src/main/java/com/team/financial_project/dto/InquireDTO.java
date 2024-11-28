package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquireDTO {
    private Integer inqId;
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
    private Integer inqCheck;
    private String inqNotice;
    // 특정 포맷 지정
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // inqCreateAt 반환
    public String getFormattedCreateAt() {
        return inqCreateAt != null ? inqCreateAt.format(FORMATTER) : null;
    }

    // inqUpdateAt 반환
    public String getFormattedUpdateAt() {
        return inqUpdateAt != null ? inqUpdateAt.format(FORMATTER) : null;
    }
}