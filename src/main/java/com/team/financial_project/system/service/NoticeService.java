package com.team.financial_project.system.service;

import com.team.financial_project.dto.InquireDTO;
import com.team.financial_project.mapper.InquireMapper;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoticeService {
    private final InquireMapper inquireMapper;

    public NoticeService(InquireMapper inquireMapper) {
        this.inquireMapper = inquireMapper;
    }

    public List<InquireDTO> findAllList() {
        List<InquireDTO> list = inquireMapper.findAllList();
        return list;
    }

    public InquireDTO findById(Long inqId) {
        InquireDTO dto = inquireMapper.findById(inqId);
        return dto;
    }
}
