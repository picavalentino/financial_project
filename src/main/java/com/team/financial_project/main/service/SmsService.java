package com.team.financial_project.main.service;

import com.team.financial_project.main.util.SmsVerificationUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class SmsService {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.sender}")
    private String sender;

    private DefaultMessageService messageService;
    private static final int MAX_ATTEMPTS = 5;
    private static final long EXPIRATION_TIME = 12; // 인증 횟수 제한 만료 시간 (12시간)
    private final RedisTemplate<String, String> redisTemplate;

    public SmsService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void initializeMessageService() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    // redis 연결 확인
    public boolean redisConnection(){
        try {
            String pong = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .ping();
            boolean ping = pong != null ? true : false;
            log.info("###########Redis 연결 상태 : "+ ping);
            return ping;
        } catch (Exception e) {
            log.info("###########Redis 연결 상태 : 연결 오류");
            return false;
        }
    }
    // 5번 인증제한
    public String canSendVerification(String phoneNumber) {
        if(redisConnection()){
            String redisKey = "sms:attempts:" + phoneNumber;
            String attempts = redisTemplate.opsForValue().get(redisKey);

            if (attempts == null) {
                // 처음 시도할 때
                if(sendVerificationCode(phoneNumber)){
                    redisTemplate.opsForValue().set(redisKey, "1", EXPIRATION_TIME, TimeUnit.HOURS);
                    return "인증번호가 발송되었습니다. 인증 시도 횟수 : 1회";
                }else {
                    return "인증번호 발송에 실패했습니다.";
                }
            } else if (Integer.parseInt(attempts) < MAX_ATTEMPTS) {
                // 5번 미만일 때 시도 가능
                if(sendVerificationCode(phoneNumber)){
                    redisTemplate.opsForValue().increment(redisKey);
                    return "인증번호가 발송되었습니다. 인증 시도 횟수 : "+ redisTemplate.opsForValue().get(redisKey) +"회";
                }else {
                    return "인증번호 발송에 실패했습니다.";
                }
            } else {
                // 5번을 초과했을 때
                return "인증 횟수를 초과하셨습니다. 12시간 후 시도해주세요.";
            }
        }else {
            return "인증번호 전송 중 오류가 발생했습니다.";
        }
    }

    // SMS 전송 로직
    public Boolean sendVerificationCode(String phoneNumber) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(phoneNumber);
        String verificationCode = SmsVerificationUtil.generateVerificationCode();
        message.setText("인증번호 : [ " + verificationCode + " ]");

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println("SMS 발송 성공: " + response);

            // 인증번호를 Redis에 저장 (유효기간 2분)
            redisTemplate.opsForValue().set(phoneNumber, verificationCode, 2, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            System.err.println("SMS 발송 실패: " + e.getMessage());
            return false;
        }
    }

    // 인증번호 일치 여부 확인 메서드
    public boolean verifyCode(String recipientPhoneNumber, String inputCode) {
        String savedCode = redisTemplate.opsForValue().get(recipientPhoneNumber);
        return savedCode != null && savedCode.equals(inputCode);
    }

    // 찐 메세지 전송
    public Boolean sendCustomMessage(String phoneNumber, String messageContent) {
        String sanitizedPhoneNumber = phoneNumber.replaceAll("-", "");

        System.out.println(sanitizedPhoneNumber);

        Message message = new Message();
        message.setFrom(sender);  // 발신자 번호 설정
        message.setTo(sanitizedPhoneNumber);  // "-"가 제거된 수신자 번호
        message.setText(messageContent);  // 메시지 내용 설정

        try {
            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println("SMS 발송 성공: " + response);
            return true;
        } catch (Exception e) {
            System.err.println("SMS 발송 실패: " + e.getMessage());
            return false;
        }
    }
}
