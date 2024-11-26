package com.team.financial_project.mypage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Configuration {
    @Bean
    public S3Client s3Client() {
        // AWS 자격 증명 및 S3 클라이언트 생성
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "YOUR_AWS_ACCESS_KEY",
                "YOUR_AWS_SECRET_KEY"
        );

        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전 설정
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

}
