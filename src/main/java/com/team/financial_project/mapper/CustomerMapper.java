package com.team.financial_project.mapper;

import com.team.financial_project.customer.dto.CustomerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper {

    // 고객 목록 조회
    @Select("SELECT * FROM customer")
    List<CustomerDTO> getAllCustomers();

    // 고객 ID로 상세 조회
    @Select("SELECT * FROM customer WHERE cust_id = #{custId}")
    CustomerDTO getCustomerById(String custId);

    // 고객 추가
    void insertCustomer(CustomerDTO customer);

    // 고객 정보 수정
    void updateCustomer(CustomerDTO customer);

    // 고객 삭제
    void deleteCustomer(String custId);

}
