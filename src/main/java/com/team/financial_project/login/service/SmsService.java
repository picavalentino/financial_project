package com.team.financial_project.login.service;

import com.team.financial_project.main.util.SmsVerificationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsService {
    @Value("${aligo.api.key}")
    private String apiKey;

    @Value("${aligo.api.user_id}")
    private String userId;

    @Value("${aligo.api.sender}")
    private String senderPhoneNumber;

    private final long CODE_EXPIRATION_TIME = 300; // 5분

    private final RedisTemplate<String, String> redisTemplate;

    public SmsService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 인증 코드 저장 (Redis)
    public void storeVerificationCode(String phoneNumber, String code) {
        redisTemplate.opsForValue().set(phoneNumber, code, CODE_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    // 인증 코드 검증
    public boolean verifyCode(String phoneNumber, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        return storedCode != null && storedCode.equals(inputCode);
    }

    // SMS 전송 로직 (알리고 API 사용)
    public String sendVerificationCode(String phoneNumber) {
        String verificationCode = SmsVerificationUtil.generateVerificationCode();
        storeVerificationCode(phoneNumber, verificationCode);  // 인증 코드 저장

        String apiUrl = "https://apis.aligo.in/send/";

        Map<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("user_id", userId);
        params.put("sender", senderPhoneNumber);
        params.put("receiver", phoneNumber);
        params.put("msg", "Your verification code is: " + verificationCode);

        try {
            RestTemplate restTemplate = restTemplate();
            String response = restTemplate.postForObject(apiUrl, params, String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send SMS";
        }
    }

}
