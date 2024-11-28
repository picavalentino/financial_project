package com.team.financial_project.system.service;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.dto.ProductDTO;
import com.team.financial_project.mapper.InquireMapper;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NoticeService {
    private final InquireMapper inquireMapper;

    public NoticeService(InquireMapper inquireMapper) {
        this.inquireMapper = inquireMapper;
    }

    // 매핑 데이터 정의
    private static final Map<String, String> CATEGORY_TYPE_MAP = Map.of(
            "1", "공지사항",
            "2", "시스템관련문의",
            "3", "기타건의사항"
    );

    // 단일 InquireDTO에 매핑 적용
    private void applyMappingsToInquire(InquireDTO inquire) {
        if (inquire.getInqCategory() != null) {
            inquire.setInqCategory(CATEGORY_TYPE_MAP.getOrDefault(inquire.getInqCategory(), inquire.getInqCategory()));
        }
    }

    // 전체 InquireDTO 리스트에 매핑 적용
    private void applyMappingsToInquires(List<InquireDTO> inquires) {
        for (InquireDTO inquire : inquires) {
            applyMappingsToInquire(inquire);
        }
    }

    public List<InquireDTO> findAllList() {
        List<InquireDTO> list = inquireMapper.findAllList();
        applyMappingsToInquires(list);
        return list;
    }

    public List<InquireDTO> searchInquires(Map<String, Object> searchParams) {
        List<InquireDTO> list = inquireMapper.searchInquiresByParams(searchParams);
        applyMappingsToInquires(list);
        return list;
    }

    public InquireDTO findById(Integer inqId) {
        // 조회수 증가
        inquireMapper.incrementInqCheck(inqId);
        // 상세 정보 조회
        InquireDTO dto = inquireMapper.findById(inqId);
        applyMappingsToInquire(dto);
        return dto;
    }

    public void updateNoticeStatus(Integer inqId, int i) {
        inquireMapper.updateNoticeStatus(inqId, i);
    }
}
