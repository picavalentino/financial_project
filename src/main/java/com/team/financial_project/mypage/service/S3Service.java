package com.team.financial_project.mypage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    private final String bucketName = " "; // 실제 S3 버킷 이름으로 교체


    public String uploadFile(MultipartFile profileImage) {
        try {
            // 업로드할 파일을 임시로 로컬에 저장
            File tempFile = convertMultipartFileToFile(profileImage);

            // S3에 업로드할 파일 키 생성
            String fileName = "사용자이미지/" + UUID.randomUUID() + "_" + profileImage.getOriginalFilename();

            // S3에 파일 업로드 요청
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, tempFile.toPath());

            // 업로드 후 임시 파일 삭제
            Files.deleteIfExists(tempFile.toPath());

            // S3 파일 URL 반환
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();

        } catch (S3Exception e) {
            e.printStackTrace();
            throw new RuntimeException("S3 업로드 실패: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
        }
    }

    // MultipartFile을 File로 변환하는 메서드
    private File convertMultipartFileToFile(MultipartFile file) throws Exception {
        File tempFile = File.createTempFile("upload_", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }
        return tempFile;
    }



}





