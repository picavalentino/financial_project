package com.team.financial_project.counsel.mapper;

import com.team.financial_project.counsel.dto.TbCounselDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CounselMapper {

    // MyBatis Test
    int checkConnection();

    // 상담 목록 페이징
    List<TbCounselDTO> getPagedCounselData(
            @Param("limit") int limit,
            @Param("offset") int offset
    );


}
