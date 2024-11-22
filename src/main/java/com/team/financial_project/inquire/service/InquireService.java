package com.team.financial_project.inquire.service;

import com.team.financial_project.mapper.InquireMapper;
import org.springframework.stereotype.Service;

@Service
public class InquireService {
    private final InquireMapper inquireMapper;

    public InquireService(InquireMapper inquireMapper) {
        this.inquireMapper = inquireMapper;
    }
}
