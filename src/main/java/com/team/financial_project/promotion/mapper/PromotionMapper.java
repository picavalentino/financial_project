package com.team.financial_project.promotion.mapper;

import com.team.financial_project.promotion.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PromotionMapper {

    // MyBatis Test
    int checkConnection();

    // 페이지에 출력하기 위한 코드 리스트 조회
    List<CodeDto> getCodeListByCl(@Param("codeCl") String codeCl);

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


    // 상품 정보 리스트 조회
    List<ProductInfoDto> getProductList(@Param("prodCd") String prodCd, @Param("prodNm") String prodNm);

    // 고객 정보 리스트 조회
    List<UserInfoDto> getUserInfoList(
            @Param("custNm") String custNm, @Param("custTelno") String custTelno);


    void insertCustprodDsgn(SavingsSaveDto savingsSaveDto);

    void insertProdDsgnSavg(SavingsSaveDto savingsSaveDto);
}
