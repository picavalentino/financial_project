package com.team.financial_project.mapper;

import com.team.financial_project.dto.CustomerAndUserCrossDTO;
import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    // 고객 , 담당자 목록 조회 [페이징 처리]
    List<CustomerAndUserCrossDTO> getCustomersUsersWithPagination(@Param("limit") int limit, @Param("offset") int offset);

    // 총 고객 수 조회
    int getTotalCustomerCount();

    // 고객 아이디 확인
    CustomerAndUserCrossDTO getCustomerById(@Param("custId") String custId);

    List<CustomerDTO> getCustomer();
}
