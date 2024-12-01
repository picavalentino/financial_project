package com.team.financial_project.customer.service;

import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.CustomerUpdateHistoryDTO;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.CustomerMapper;
import com.team.financial_project.mapper.InquireMapper;
import com.team.financial_project.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InquireMapper inquireMapper;

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(CustomerMapper customerMapper, UserMapper userMapper, InquireMapper inquireMapper) {
        this.customerMapper = customerMapper;
        this.userMapper = userMapper;
        this.inquireMapper = inquireMapper;
    }

    // 담당자 목록 조회
    public List<UserDTO> getManagersByName(String name) {
        return customerMapper.findManagersByName(name);
    }

    // ======================================================================================================
    // 검색 조건이 있는 고객 목록 조회
    public List<CustomerDTO> searchCustomersWithPagination(int page, int pageSize, String searchType, String keyword, String status) {
        int offset = (page - 1) * pageSize;
        List<CustomerDTO> customers = customerMapper.searchCustomers(pageSize, offset, searchType, keyword, status);

        // 각 고객의 생년월일 추출
        for (CustomerDTO customer : customers) {
            customer.extractBirthDateFromRrn();
        }

        return customers;
    }

    // 검색 조건 없는 고객 목록 조회
    public List<CustomerDTO> getCustomersWithPagination(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<CustomerDTO> customers = customerMapper.getCustomersWithPagination(pageSize, offset);

        // 각 고객의 생년월일 추출
        for (CustomerDTO customer : customers) {
            customer.extractBirthDateFromRrn();
        }

        return customers;
    }

    // 검색 조건에 따른 총 고객 수 계산
    public int getTotalCustomerCount(String searchType, String keyword, String status) {
        return customerMapper.getCustomerCount(searchType, keyword, status);
    }

    // ======================================================================================================
    // 고객 상세 페이지로 이동
    public CustomerDTO getCustomerById(String custId) {
        return customerMapper.getCustomerById(custId);
    }

    // ======================================================================================================
    // 고객 등록 페이지

    @Transactional
    public void insertCustomer(CustomerDTO customerDto) {

        // 오늘 날짜를 yyyyMMdd 형식으로 생성
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = today.format(formatter);

        // CustomerDTO의 users 객체가 null인지 확인 후 초기화
        if (customerDto.getUsers() == null) {
            customerDto.setUsers(new UserDTO());
        }

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
    public List<CustomerDTO> getCustOccpTyCdList() {
        log.info("직업 코드 리스트 조회 요청");
        return customerMapper.getCustOccpTyCdList();
    }

    // 고객정보 수정
    @Transactional
    public void updateCustomer(CustomerDTO customerDTO, String staffId) {
        // 기존 고객 정보 가져오기
        CustomerDTO originalCustomer = customerMapper.getCustomerById(customerDTO.getCustId());

        // 고객 정보 업데이트
        customerMapper.updateCustomer(customerDTO);

        // 수정 내역 생성 및 저장
        createAndSaveHistory(originalCustomer, customerDTO, staffId);
    }
    @Transactional
    public List<CustomerUpdateHistoryDTO> createAndSaveHistory(CustomerDTO original, CustomerDTO updated, String staffId) {
        List<CustomerUpdateHistoryDTO> historyList = new ArrayList<>();

        compareAndAddHistory(historyList, "전화번호", original.getCustTelno(), updated.getCustTelno(), updated.getCustId(), staffId);
        compareAndAddHistory(historyList, "이메일", original.getCustEmail(), updated.getCustEmail(), updated.getCustId(), staffId);
        compareAndAddHistory(historyList, "주소", original.getCustAddr(), updated.getCustAddr(), updated.getCustId(), staffId);
        compareAndAddHistory(historyList, "직업 코드", original.getCustOccpTyCd(), updated.getCustOccpTyCd(), updated.getCustId(), staffId);

        if (!historyList.isEmpty()) {
            customerMapper.insertHistoryById(historyList);
        }

        return historyList;
    }
    @Transactional
    private void compareAndAddHistory(List<CustomerUpdateHistoryDTO> historyList, String fieldName, String originalValue, String updatedValue, String custId, String staffId) {
        if (!Objects.equals(originalValue, updatedValue)) {
            CustomerUpdateHistoryDTO history = new CustomerUpdateHistoryDTO();
            history.setCustId(custId);
            history.setUserId(staffId);
            history.setUpdateDetail(String.format("%s: %s → %s", fieldName,
                    originalValue != null ? originalValue : "null",
                    updatedValue != null ? updatedValue : "null"));
            history.setCustUpdateAt(LocalDateTime.now());
            historyList.add(history);
        }
    }
    @Transactional
    public List<CustomerUpdateHistoryDTO> getCustomerHistoryById(String custId) {
        return customerMapper.findCustomerHistoryListById(custId);
    }
}

