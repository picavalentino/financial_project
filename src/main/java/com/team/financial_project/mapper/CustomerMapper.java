package com.team.financial_project.mapper;

import com.team.financial_project.dto.CustomerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    // 고객 목록 조회 [ 페이징 처리 ]
    List<CustomerDTO> getCustomersWithPagination(@Param("limit") int limit, @Param("offset") int offset);

    // 총 고객 수 조회
    int getTotalCustomerCount();

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
