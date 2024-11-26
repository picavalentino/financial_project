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
    private String custOccpTyCd_name; // 고객 직업 분류명
    private String custAddr; // 고객 주소
    private LocalDateTime custCreateAt; // 고객 생성일시
    private LocalDateTime custUpdateAt; // 고객 수정일시
    private String custStateCd; // 고객 상태 코드
    private String birthDate; // 생년월일 필드 추가

    private UserDTO users; // 유저 필드 추가


    // 주민등록번호에서 생년월일 추출하는 메소드
    public void extractBirthDateFromRrn() {
        if (custRrn != null && custRrn.matches("\\d{6}-\\d{7}")) {
            String rrnPrefix = custRrn.split("-")[0]; // 예: 990101
            char genderCode = custRrn.split("-")[1].charAt(0); // 성별 구분 코드 (1, 2, 3, 4)

            if (genderCode == '1' || genderCode == '2') {
                this.birthDate = "19" + rrnPrefix; // 1900년대 출생
            } else if (genderCode == '3' || genderCode == '4') {
                this.birthDate = "20" + rrnPrefix; // 2000년대 출생
            } else {
                this.birthDate = ""; // 유효하지 않은 경우 빈 문자열 설정
            }
        } else {
            this.birthDate = ""; // 주민등록번호가 없거나 형식이 맞지 않는 경우 빈 문자열 설정
        }
    }
}
