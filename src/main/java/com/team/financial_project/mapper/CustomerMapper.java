package com.team.financial_project.mapper;

import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.CustomerUpdateHistoryDTO;
import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper {

    List<UserDTO> findManagersByName(@Param("name") String name);

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

    // 고객 수정 내역 삽입

    @Insert("""
        INSERT INTO tb_cust_update_hist (cust_id, user_id, update_detail, cust_update_at) 
        VALUES (#{custId}, #{userId}, #{updateDetail}, #{custUpdateAt})
    """)
    void insertUpdateHistory(CustomerUpdateHistoryDTO history);

    // 고객 수정 내역 조회
    @Select("""
        SELECT * FROM tb_cust_update_hist 
        WHERE cust_id = #{custId} 
        ORDER BY cust_update_at DESC
    """)
    List<CustomerUpdateHistoryDTO> findUpdateHistoryByCustId(@Param("custId") String custId);
}
