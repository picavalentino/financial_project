package com.team.financial_project.mapper;

import com.team.financial_project.dto.CustomerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    // 검색 조건이 있는 고객 목록 조회
    List<CustomerDTO> searchCustomers(@Param("pageSize") int pageSize,
                                      @Param("offset") int offset,
                                      @Param("searchType") String searchType,
                                      @Param("keyword") String keyword);

    // 검색 조건 없는 페이징 처리된 고객 목록 조회
    List<CustomerDTO> getCustomersWithPagination(@Param("limit") int limit, @Param("offset") int offset);

    // 검색 조건에 따른 총 고객 수 계산
    int getCustomerCount(@Param("searchType") String searchType,
                         @Param("keyword") String keyword);
    // ==============================================================================================================
    // 고객 선택 조회
    void resetAllCustomerStatus(String unselected);

    void updateCustomerStatus(List<String> selectedCustomers, String selected);

    void resetCustomerStatusExcept(List<String> selectedCustomers, String unselected);
    // ==============================================================================================================

    // 고객 아이디 확인
    CustomerDTO getCustomerById(@Param("custId") String custId);

    // 고객 등록
    void insertCustomer(CustomerDTO customerDto);

    // 오늘 등록된 고객 수 조회
    int countTodayCustomers();

    // 고객 정보 수정
    void updateCustomer(CustomerDTO customerDTO);

    // 고객 직업 코드
    List<CustomerDTO> getCustOccpTyCdList();
}
