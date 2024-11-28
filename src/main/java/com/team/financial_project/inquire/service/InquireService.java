package com.team.financial_project.inquire.service;

import com.team.financial_project.dto.InquireCommentDTO;
import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.main.service.S3Service;
import com.team.financial_project.mapper.InquireMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class InquireService {
    private final InquireMapper inquireMapper;
    private final S3Service s3Service;

    public InquireService(InquireMapper inquireMapper, S3Service s3Service) {
        this.inquireMapper = inquireMapper;
        this.s3Service = s3Service;
    }

    // 게시글 READ
    public String getUserName(String userId) {
        return inquireMapper.getUserName(userId);
    }

    public List<InquireDTO> searchInquires(String category, String keywordType, String keyword, String createAt, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return inquireMapper.searchInquires(category, keywordType, keyword, createAt, offset, pageSize);
    }

    public int countSearchInquires(String category, String keywordType, String keyword, String createAt) {
        return inquireMapper.countSearchInquires(category, keywordType, keyword, createAt);
    }

    public InquireDTO getInquireById(Integer inqId) {
        return inquireMapper.getInquireById(inqId);
    }

    // 게시글 저장
    public void saveInquire(InquireDTO inquireDTO) {
        inquireMapper.insertInquire(inquireDTO);
    }

    public String uploadFileToS3(MultipartFile file) throws IOException {
        return s3Service.uploadFile(file);
    }

    // 게시글 수정

    // 게시글 삭제
    public void deleteInquire(Integer inqId) {
        inquireMapper.deleteInquire(inqId);
    }

    // 댓글 CRUD
    public List<InquireCommentDTO> getCommentsByInqId(Integer inqId) {
        return inquireMapper.getCommentsByInqId(inqId);
    }
    // user_auth_cd 조회 메서드
    public String getUserAuthCdByUserId(String userId) {
        return inquireMapper.getUserAuthCdByUserId(userId);
    }

    public void updateInqReply(Integer inqId, String inqReply) {
        inquireMapper.updateInqReply(inqId, inqReply);
    }

    public void saveComment(InquireCommentDTO commentDTO) {
        inquireMapper.insertComment(commentDTO);
    }

    public void updateInquire(InquireDTO inquireDTO) {
        inquireMapper.updateInquire(inquireDTO);
    }
}