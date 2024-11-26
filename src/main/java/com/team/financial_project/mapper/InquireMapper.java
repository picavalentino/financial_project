package com.team.financial_project.mapper;

import com.team.financial_project.dto.InquireDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquireMapper {
    List<InquireDTO> findAllList();

    InquireDTO findById(Long inqId);
}
