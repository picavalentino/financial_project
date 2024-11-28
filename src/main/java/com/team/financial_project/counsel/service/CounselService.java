package com.team.financial_project.counsel.service;

import com.team.financial_project.counsel.dto.CodeDTO;
import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.counsel.mapper.CounselMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CounselService {
    private final CounselMapper mapper;

    public CounselService(CounselMapper mapper) {
        this.mapper = mapper;
    }

    // MyBatis Test
    public int checkConnection() {
        return mapper.checkConnection();
    }

    // 페이지에 출력하기 위한 코드 리스트 조회
    public List<CodeDTO> getCodeListByCl(String codeCl) {
        return mapper.getCodeListByCl(codeCl);
    }

    public Map<String, Object> getPagedCounselData(int page, int size) {
        int offset = (page - 1) * size;  // 페이지 매김에 대한 오프셋 계산

        // 페이지를 매긴 데이터 가져오기
        List<TbCounselDTO> content = mapper.getPagedCounselData(size, offset);

        // 페이지 매김 정보의 총 개수 가져오기
        long totalElements = mapper.getTotalCounselCount();

        // 총 페이지 계산
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Prepare response map
        Map<String, Object> response = new HashMap<>();
        response.put("content", content);  // The paginated data
        response.put("totalPages", totalPages);  // Total number of pages
        response.put("number", page);  // Current page
        response.put("size", size);  // Page size
        response.put("totalElements", totalElements);  // Total number of records

        return response;
    }

    public void insertCounsel(TbCounselDTO dto) {
        mapper.insertCounsel(dto);
    }

    public void updateCounsel(Long id, String category, String content) {
        mapper.updateCounsel(id, category, content);
    }

    
    
    public void deleteCounsel(Long id) {
        mapper.deleteCounsel(id);
    }

    public TbCounselDTO getCounselById(Long id) {
        return mapper.getCounselById(id);
    }
}
