package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquireCommentDTO {
    private Integer commentId;
    private Integer inqId;
    private String userId;
    private String commentAnonym;
    private String commentContent;
    private LocalDateTime commentCreateAt;
    private String userAuthCd;
    private String userName;

    // 특정 포맷 지정
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // commentCreateAt 반환
    public String getFormattedCreateAt() {
        return commentCreateAt != null ? commentCreateAt.format(FORMATTER) : null;
    }
}
