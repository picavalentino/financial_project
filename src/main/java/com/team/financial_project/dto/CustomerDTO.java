package com.team.financial_project.dto;

import com.team.financial_project.customer.Enum.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String custId; // 고객 ID
    private String custNm; // 고객 이름
    private String custRrn; // 고객 주민등록번호 (RRN)
    private String custTelno; // 고객 전화번호
    private String custEmail; // 고객 이메일
    private String custOccpTyCd; // 고객 직업 코드
    private String custAddr; // 고객 주소
    private LocalDateTime custCreateAt; // 고객 생성일시
    private LocalDateTime custUpdateAt; // 고객 수정일시
    private CustomerStatus custStateCd = CustomerStatus.ACTIVE; // 고객 상태 코드 (enum)
}
