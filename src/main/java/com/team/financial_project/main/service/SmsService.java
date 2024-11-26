package com.team.financial_project.main.service;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SmsService {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.sender}")
    private String sender;

    private DefaultMessageService messageService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final int MAX_ATTEMPTS = 5;
    private static final long EXPIRATION_TIME = 24; // 인증 횟수 제한 만료 시간 (24시간)

    public SmsService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void initializeMessageService() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }
    // 6자리 랜덤 인증번호 생성 메서드
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999 범위의 숫자 생성
        return String.valueOf(code);
    }

    // 5번 인증제한
    public boolean canSendVerification(String phoneNumber) {
        String redisKey = "sms:attempts:" + phoneNumber;
        String attempts = redisTemplate.opsForValue().get(redisKey);

        if (attempts == null) {
            // 처음 시도할 때
            redisTemplate.opsForValue().set(redisKey, "1", EXPIRATION_TIME, TimeUnit.HOURS);
            return true;
        } else if (Integer.parseInt(attempts) < MAX_ATTEMPTS) {
            // 5번 미만일 때 시도 가능
            redisTemplate.opsForValue().increment(redisKey);
            return true;
        } else {
            // 5번을 초과했을 때
            return false;
        }
    }

    // SMS 전송 로직
    public String sendVerificationCode(String phoneNumber) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(phoneNumber);
        String verificationCode = generateVerificationCode();
        message.setText("인증번호 : [ " + verificationCode + " ]");

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println("SMS 발송 성공: " + response);

            // 인증번호를 Redis에 저장 (유효기간 5분)
            redisTemplate.opsForValue().set(phoneNumber, verificationCode, 2, TimeUnit.MINUTES);
            return "인증번호를 발송했습니다.";
        } catch (Exception e) {
            System.err.println("SMS 발송 실패: " + e.getMessage());
            return "인증번호를 발송에 실패했습니다.";
        }
    }

    // 인증번호 일치 여부 확인 메서드
    public boolean verifyCode(String recipientPhoneNumber, String inputCode) {
        String savedCode = redisTemplate.opsForValue().get(recipientPhoneNumber);
        return savedCode != null && savedCode.equals(inputCode);
    }
}
