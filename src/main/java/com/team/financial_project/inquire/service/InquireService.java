package com.team.financial_project.inquire.service;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.main.service.S3Service;
import com.team.financial_project.mapper.InquireMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class InquireService {
    private final InquireMapper inquireMapper;
    private final S3Service s3Service;

    public InquireService(InquireMapper inquireMapper, S3Service s3Service) {
        this.inquireMapper = inquireMapper;
        this.s3Service = s3Service;
    }

    public void saveInquire(InquireDTO inquireDTO) {
        inquireMapper.insertInquire(inquireDTO);
    }

    public String getUserName(String userId) {
        return inquireMapper.getUserName(userId);
    }

    public String uploadFileToS3(MultipartFile file) throws IOException {
        return s3Service.uploadFile(file);
    }

}
