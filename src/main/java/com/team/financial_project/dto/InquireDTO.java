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
    private Integer inqCheck;
    private String inqNotice;
    private String inqAttachFile1;
    private String inqAttachFile2;
    private String inqAttachFile3;
    private String inqAttachFile4;
    private String inqAttachFile5;
    private String inqStatus;
    private LocalDateTime inqCreateAt;
    private LocalDateTime inqUpdateAt;

    public String getFormattedInqCreateAt() {
        if (inqCreateAt != null) {
            return inqCreateAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return null; // 날짜가 없는 경우 처리
    }
}
