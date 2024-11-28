package com.team.financial_project.counsel.mapper;

import com.team.financial_project.counsel.dto.CodeDTO;
import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CounselMapper {

    // MyBatis Test
    int checkConnection();

    // 페이지에 출력하기 위한 코드 리스트 조회
    List<CodeDTO> getCodeListByCl(@Param("codeCl") String codeCl);

    // 상담 전체 내역 가져오기
    List<TbCounselDTO> getPagedCounselData(@Param("limit") int limit, @Param("offset") int offset);

    // 총 레코드 수 가져오기
    long getTotalCounselCount();

    // 로그인된 유저 아이디로 정보 가져오기
    UserDTO getUserById(@Param("userId") String userId);

    // 상담 작성
    void insertCounsel(@Param("dto") TbCounselDTO dto);

    void updateCounsel(@Param("id") Long id, @Param("category") String category, @Param("content") String content);

    void deleteCounsel(@Param("id") Long id);

    TbCounselDTO getCounselById(@Param("id") Long id);

    // 특정 고객 상담 내역 가져오기
    List<TbCounselDTO> getCounselByCustomerId(Map<String, Object> params);

    // 특정 고객 상담 - 총 레코드 수 가져오기
    long getTotalCounselCountByCustomerId(String custId);
}
