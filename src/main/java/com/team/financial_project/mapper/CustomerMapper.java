package com.team.financial_project.mapper;

import com.team.financial_project.dto.CustomerDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {

    List<CustomerDTO> getAllCustomers();
}
