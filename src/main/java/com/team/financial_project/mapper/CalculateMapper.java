package com.team.financial_project.mapper;

import com.team.financial_project.financingCalculate.dto.CalculateProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CalculateMapper {

    List<CalculateProductDTO> findSavg();
}
