package com.team.financial_project.mapper;

import com.team.financial_project.dto.CodeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeMapper {
    List<CodeDTO> getAllAuth();
    List<CodeDTO> getAllStatus();
}
