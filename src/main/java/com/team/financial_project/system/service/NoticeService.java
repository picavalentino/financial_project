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

    public void updateNoticeStatus(Integer inqId, String status) {
        inquireMapper.updateNoticeStatus(inqId, status);
    }

    public Map<String, Object> getNotices(String inqCategory, String keywordType, String keyword,
                                          String inqCreateAt, int page, int size,
                                          String sortColumn, String sortDirection) {
        // 기본값 설정
        page = Math.max(page, 1); // 최소 1 페이지
        size = Math.max(size, 1); // 최소 1개 데이터

        int totalItems; // 총 게시글 개수

        // 검색 조건이 있을 경우와 없을 경우를 분리
        if (inqCategory != null || keywordType != null || keyword != null || inqCreateAt != null) {
            // 조건 검색인 경우
            totalItems = inquireMapper.countSearchInquires(inqCategory, keywordType, keyword, inqCreateAt);
        } else {
            // 조건이 없는 처음 로딩의 경우
            totalItems = inquireMapper.countAllList();
        }

        // 페이징 처리
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int startIndex = (page - 1) * size;

        // 게시글 가져오기
        List<InquireDTO> list;
        if (inqCategory != null || keywordType != null || keyword != null || inqCreateAt != null) {
            list = inquireMapper.searchInquiresAll(inqCategory, keywordType, keyword, inqCreateAt);
        } else {
            list = inquireMapper.findAllList(); // 처음 로딩 데이터
        }

        // 정렬 로직
        Comparator<InquireDTO> comparator = Comparator.comparing((InquireDTO inquire) -> "1".equals(inquire.getInqNotice()) ? 0 : 1);
        if (sortColumn != null && sortDirection != null) {
            Comparator<InquireDTO> secondaryComparator = getComparator(sortColumn, sortDirection);
            if (secondaryComparator != null) {
                comparator = comparator.thenComparing(secondaryComparator);
            }
        }
        list.sort(comparator);

        // 페이징된 데이터 추출
        int endIndex = Math.min(startIndex + size, totalItems);
        List<InquireDTO> paginatedList = (startIndex >= 0 && startIndex < totalItems)
                ? list.subList(startIndex, endIndex)
                : List.of();

        // 결과 반환
        Map<String, Object> result = new HashMap<>();
        result.put("list", paginatedList);
        result.put("totalPages", Math.max(totalPages, 1)); // 최소 1페이지
        result.put("totalItems", totalItems);
        return result;
    }

    private Comparator<InquireDTO> getComparator(String sortColumn, String sortDirection) {
        if (sortColumn == null || sortColumn.isEmpty()) {
            return null; // 기본 정렬
        }

        Comparator<InquireDTO> comparator;
        switch (sortColumn) {
            case "inqId":
                comparator = Comparator.comparing(InquireDTO::getInqId);
                break;
            case "inqCategory":
                comparator = Comparator.comparing(InquireDTO::getInqCategory);
                break;
            case "userId":
                comparator = Comparator.comparing(InquireDTO::getUserId);
                break;
            case "inqTitle":
                comparator = Comparator.comparing(InquireDTO::getInqTitle);
                break;
            case "inqCreateAt":
                comparator = Comparator.comparing(InquireDTO::getInqCreateAt);
                break;
            case "inqCheck":
                comparator = Comparator.comparing(InquireDTO::getInqCheck);
                break;
            case "inqReply":
                comparator = Comparator.comparing(InquireDTO::getInqReply);
                break;
            case "inqNotice":
                comparator = Comparator.comparing(InquireDTO::getInqNotice);
                break;
            default:
                comparator = Comparator.comparing(InquireDTO::getInqId); // 기본 정렬
        }

        // Reverse order if descending
        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

}
