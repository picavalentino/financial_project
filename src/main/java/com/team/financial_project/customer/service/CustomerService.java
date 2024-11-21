package com.team.financial_project.customer.service;

import com.team.financial_project.dto.CustomerDTO;
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


    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.getAllCustomers();
    }
    // ======================================================================================================
}
