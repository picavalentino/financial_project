package com.team.financial_project.main.util;

import java.security.SecureRandom;

public class SmsVerificationUtil {
    private static final String CHARACTERS = "0123456789"; // 랜덤 코드에 쓰일 문자 설정
    private static final int CODE_LENGTH = 6; // 랜덤 코드 길이

    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
