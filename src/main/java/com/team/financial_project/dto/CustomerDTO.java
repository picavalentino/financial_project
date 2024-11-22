package com.team.financial_project.dto;

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
    private String custStateCd; // 고객 상태 코드

    private String birthDate; // 생년월일 필드 추가
    private String custAddrDetail; // 상세 주소 필드 추가

    private UserDTO users; // 유저 필드 추가

    // 주소를 나누는 메소드
    public void splitAddress() {
        if (custAddr != null) {
            String[] splitAddr = custAddr.split(" ", 2);
            if (splitAddr.length == 2) {
                this.custAddr = splitAddr[0]; // 기본 주소 (예: '서울특별시')
                this.custAddrDetail = splitAddr[1]; // 나머지 상세 주소 (예: '강남구 테헤란로 123')
            } else {
                this.custAddrDetail = ""; // 나머지 주소가 없는 경우 빈 문자열 설정
            }
        }
    }

    // 주민등록번호에서 생년월일 추출하는 메소드
    public void extractBirthDateFromRrn() {
        if (custRrn != null && custRrn.contains("-")) {
            this.birthDate = custRrn.split("-")[0]; // 주민등록번호에서 '-' 앞의 생년월일 부분만 추출
        } else {
            this.birthDate = ""; // 주민등록번호가 없거나 형식이 맞지 않는 경우 빈 문자열 설정
        }
    }
}
