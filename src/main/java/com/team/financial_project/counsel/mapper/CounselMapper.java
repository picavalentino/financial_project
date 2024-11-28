package com.team.financial_project.counsel.mapper;

import com.team.financial_project.counsel.dto.CodeDTO;
import com.team.financial_project.counsel.dto.TbCounselDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CounselMapper {

    // MyBatis Test
    int checkConnection();

    // 페이지에 출력하기 위한 코드 리스트 조회
    List<CodeDTO> getCodeListByCl(@Param("codeCl") String codeCl);

    List<TbCounselDTO> getPagedCounselData(@Param("limit") int limit, @Param("offset") int offset);

    // 총 레코드 수를 가져오는 사용자 정의 방법
    long getTotalCounselCount();

    // 상담 작성
    void insertCounsel(@Param("dto") TbCounselDTO dto);

    void updateCounsel(@Param("id") Long id, @Param("category") String category, @Param("content") String content);

    void deleteCounsel(@Param("id") Long id);

    TbCounselDTO getCounselById(@Param("id") Long id);
}
