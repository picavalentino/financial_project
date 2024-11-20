package com.team.financial_project.counsel.service;

import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.counsel.entity.TbCounsel;
import com.team.financial_project.counsel.mapper.CounselMapper;
import com.team.financial_project.counsel.repository.CounselRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CounselService {
    private final CounselMapper mapper;

    @Autowired
    CounselRepository counselRepository;

    public CounselService(CounselMapper mapper) {
        this.mapper = mapper;
    }

    // MyBatis Test
    public int checkConnection() {
        return mapper.checkConnection();
    }

    public List<TbCounselDTO> getPagedCounselData(int page, int size) {
        int offset = (page - 1) * size;
        List<TbCounselDTO> dtos = mapper.getPagedCounselData(size, offset);
        return dtos;
    }
}
