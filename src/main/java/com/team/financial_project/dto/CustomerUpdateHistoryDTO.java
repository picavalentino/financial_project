package com.team.financial_project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerUpdateHistoryDTO {
    private int updateId;                 // 고유 ID
    private String custId;          // 고객 ID
    private String userId;          // 수정자 ID
    private String updateDetail;    // 수정 내용
    private LocalDateTime custUpdateAt; // 수정 시간
}
