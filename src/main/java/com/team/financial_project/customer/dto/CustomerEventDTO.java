package com.team.financial_project.customer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerEventDTO {
    private Long eventId; // 이벤트 ID (BIGINT 매핑)
    private String custId; // 고객 ID
    private String userId; // 사용자 ID
    private String eventAnydayYmd; // 이벤트 일자 (YYYYMMDD 형식으로 관리)
    private String eventTypeCode; // 이벤트 유형 코드
    private String eventWriterName; // 이벤트 작성자 이름
    private LocalDateTime eventCreatedAt; // 이벤트 생성일시
    private LocalDateTime eventUpdatedAt; // 이벤트 수정일시
}
