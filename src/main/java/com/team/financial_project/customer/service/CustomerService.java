package com.team.financial_project.customer.service;

import com.team.financial_project.dto.CustomerAndUserCrossDTO;
import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.UserDTO;
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
    // 페이징 처리된 고객 목록 / 담당자 조회
    public List<CustomerAndUserCrossDTO> getCustomersUsersWithPagination(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return customerMapper.getCustomersUsersWithPagination(pageSize, offset);
    }

    public List<CustomerDTO> getCustomersBirth(){
        List<CustomerDTO> customers = customerMapper.getCustomer();
           for (CustomerDTO customer : customers) {
            customer.extractBirthDateFromRrn(); // 각 고객의 생년월일을 추출
        }
           return customers;
    }

    // 총 고객 수 조회
    public int getTotalCustomerCount() {
        return customerMapper.getTotalCustomerCount();
    }

    // ======================================================================================================
    // 고객 상세 페이지로 이동
    public CustomerAndUserCrossDTO getCustomerById(String custId) {
        return customerMapper.getCustomerById(custId);
    }
}
