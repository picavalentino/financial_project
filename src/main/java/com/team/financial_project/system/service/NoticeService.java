package com.team.financial_project.system.service;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.dto.ProductDTO;
import com.team.financial_project.mapper.InquireMapper;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    public Map<String, Object> getNotices(String inqCategory, String keywordType, String keyword,
                                          String inqCreateAt, int page, int size,
                                          String sortColumn, String sortDirection) {
        // 검색 조건에 따라 데이터를 가져옴
        List<InquireDTO> list;
        if (inqCategory != null || keywordType != null || keyword != null || inqCreateAt != null) {
            list = inquireMapper.searchInquiresAll(inqCategory, keywordType, keyword, inqCreateAt);
        } else {
            list = inquireMapper.findAllList();
        }
        applyMappingsToInquires(list);

        // 정렬 로직
        if (sortColumn != null && sortDirection != null) {
            Comparator<InquireDTO> comparator = getComparator(sortColumn);
            if ("desc".equalsIgnoreCase(sortDirection) && comparator != null) {
                comparator = comparator.reversed();
            }
            if (comparator != null) {
                list.sort(comparator);
            }
        }

        // 페이징 처리
        int totalItems = list.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<InquireDTO> paginatedList = list.subList(startIndex, endIndex);

        // 결과 반환
        Map<String, Object> result = new HashMap<>();
        result.put("list", paginatedList);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalItems);
        return result;
    }

    private Comparator<InquireDTO> getComparator(String sortColumn) {
        switch (sortColumn) {
            case "inqId":
                return Comparator.comparing(InquireDTO::getInqId);
            case "inqCategory":
                return Comparator.comparing(InquireDTO::getInqCategory);
            case "userId":
                return Comparator.comparing(InquireDTO::getUserId);
            case "inqTitle":
                return Comparator.comparing(InquireDTO::getInqTitle);
            case "inqCreateAt":
                return Comparator.comparing(InquireDTO::getInqCreateAt);
            case "inqCheck":
                return Comparator.comparing(InquireDTO::getInqCheck);
            case "inqReply":
                return Comparator.comparing(InquireDTO::getInqReply);
            case "inqNotice":
                return Comparator.comparing(InquireDTO::getInqNotice);
            default:
                return Comparator.comparing(InquireDTO::getInqId);
        }
    }
}
