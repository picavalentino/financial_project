package com.team.financial_project.mapper;

import com.team.financial_project.customer.dto.CustomerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    // 고객 목록 조회 [페이징 처리]
    List<CustomerDTO> getCustomersWithPagination(@Param("limit") int limit, @Param("offset") int offset);

    // 총 고객 수 조회 (페이지 수 계산용)
    int getTotalCustomerCount();

}
