package com.team.financial_project.customer.service;

import com.team.financial_project.customer.dto.CustomerDTO;
import com.team.financial_project.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    // ======================================================================================================
    // 페이징 처리된 고객 목록 조회
    public List<CustomerDTO> getCustomersWithPagination(int page, int pageSize) {
        // 페이지 번호가 유효한지 검증 (1 이상이어야 함)
        if (page < 1) {
            page = 1; // 기본 페이지를 1로 설정
        }
        if (pageSize < 1) {
            pageSize = 10; // 기본 페이지 크기를 10으로 설정
        }

        // 페이지 오프셋 계산
        int offset = (page - 1) * pageSize;
        return customerMapper.getCustomersWithPagination(pageSize, offset);
    }

    // 총 고객 수 조회
    public int getTotalCustomerCount() {
        return customerMapper.getTotalCustomerCount();
    }
    // ======================================================================================================
}
