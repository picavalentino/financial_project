package com.team.financial_project.promotion.mapper;

import com.team.financial_project.promotion.dto.PromotionListDto;
import com.team.financial_project.promotion.dto.UserInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionMapper {

    // MyBatis Test
    int checkConnection();

    // 설계 조회 페이지
    // 전체 데이터 개수 조회
    int getTotalCount(
            @Param("prgStcd") String prgStcd,
            @Param("dsTyCd") String dsTyCd,
            @Param("custNm") String custNm,
            @Param("userNm") String userNm,
            @Param("prodNm") String prodNm
    );

    // 데이터 리스트 조회
    List<PromotionListDto> getPagedList(
            @Param("prgStcd") String prgStcd,
            @Param("dsTyCd") String dsTyCd,
            @Param("custNm") String custNm,
            @Param("userNm") String userNm,
            @Param("prodNm") String prodNm,
            @Param("sortColumn") String sortColumn, // 정렬 기준
            @Param("sortDirection") String sortDirection, // 정렬 방향
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 진행상태 업데이트 위해 모든 데이터 조회
    List<PromotionListDto> getAllPromotions();

    // 진행상태 업데이트
    void updateProgressStatus(String dsgnSn, String newStatus);


    // 금융계산기 페이지

    // 고객 정보 리스트 조회
    List<UserInfoDto> getUserInfoList(
            @Param("custNm") String custNm, @Param("custTelno") String custTelno);

}
