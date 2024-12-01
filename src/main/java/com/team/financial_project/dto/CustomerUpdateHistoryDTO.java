package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateHistoryDTO {
    private int updateId;                 // 고유 ID
    private String custId;          // 고객 ID
    private String userId;          // 수정자 ID
    private String updateDetail;    // 수정 내용
    private LocalDateTime custUpdateAt; // 수정 시간


    @Override
    public String toString() {
        return String.format(
                "수정 일시: %s\n수정 ID: %s\n내용: %s\n======================================",
                custUpdateAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                userId,
                updateDetail
        );
    }
}
