package com.team.financial_project.customer.service;

import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.CustomerMapper;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private UserMapper userMapper;


    public CustomerService(CustomerMapper customerMapper, UserMapper userMapper) {
        this.customerMapper = customerMapper;
        this.userMapper = userMapper;
    }

    // ======================================================================================================
    // 페이징 처리된 고객 목록 조회
    public List<CustomerDTO> getCustomersWithPagination(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<CustomerDTO> customers = customerMapper.getCustomersWithPagination(pageSize, offset);
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
    public CustomerDTO getCustomerById(String custId) {
        return customerMapper.getCustomerById(custId);
    }

    // ======================================================================================================
    // 고객 등록 페이지
    public void insertCustomer(CustomerDTO customerDto) {

        // 오늘 날짜를 yyyyMMdd 형식으로 생성
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = today.format(formatter);

        // 오늘 등록된 고객 수 조회
        int todayCount = customerMapper.countTodayCustomers();

        // 오늘 생성된 고객 순번 생성 (001, 002, ...)
        String countPart = String.format("%03d", todayCount + 1);

        // 최종 고객 ID 조합
        String customerId = datePart + countPart;

        // 고객 DTO에 ID 설정
        customerDto.setCustId(customerId);

        // 고객 생성일시 및 수정일시 설정 (현재 시간)
        LocalDateTime now = LocalDateTime.now();
        customerDto.setCustCreateAt(now);
        customerDto.setCustUpdateAt(now);

        // 고객 상태 코드 설정 (기본값: ACTIVE)
        customerDto.setCustStateCd("1");

        // 고객 정보 저장
        customerMapper.insertCustomer(customerDto);
    }
    // ==========================================================================================================
    // 고객정보 수정

    public void updateCustomer(CustomerDTO customerDTO) {
        System.out.println("수정 요청된 고객 정보: " + customerDTO);
        customerMapper.updateCustomer(customerDTO);
    }
}

