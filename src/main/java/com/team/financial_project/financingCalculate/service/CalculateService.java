package com.team.financial_project.financingCalculate.service;

import com.team.financial_project.financingCalculate.dto.CalculateProductDTO;
import com.team.financial_project.mapper.CalculateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateService {
    @Autowired
    CalculateMapper calculateMapper;

    public List<CalculateProductDTO> getAllSavgProduct() {
        return calculateMapper.findSavg();
    }
}
