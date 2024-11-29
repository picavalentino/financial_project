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
    public List<CustomerDTO> searchCustomersWithPagination(int page, int pageSize, String searchType, String keyword) {
        int offset = (page - 1) * pageSize;
        List<CustomerDTO> customers = customerMapper.searchCustomers(pageSize, offset, searchType, keyword);

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
    public int getTotalCustomerCount(String searchType, String keyword) {
        return customerMapper.getCustomerCount(searchType, keyword);
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
    // 고객정보 수정
    public void updateCustomer(CustomerDTO customerDTO) {
        try {
            log.info("수정 요청된 고객 정보: {}", customerDTO);
            customerMapper.updateCustomer(customerDTO);
        } catch (Exception e) {
            log.error("고객 정보 수정 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("고객 정보 수정에 실패했습니다.", e);
        }
    }

    public List<CustomerDTO> getCustOccpTyCdList() {
        log.info("직업 코드 리스트 조회 요청");
        return customerMapper.getCustOccpTyCdList();
    }

    public List<CustomerUpdateHistoryDTO> getCustomerHistoryById(String custId) {
        return customerMapper.findCustomerHistoryListById(custId);
    }


    public CustomerUpdateHistoryDTO createUpdateHistory(CustomerDTO customerDTO, CustomerDTO existingCustomer, String staffId) {
        // 기존 데이터 가져오기
        CustomerDTO originalCustomer = customerMapper.getCustomerById(customerDTO.getCustId());

        // 변경된 항목 기록
        StringBuilder detail = new StringBuilder();
        if (!Objects.equals(originalCustomer.getCustTelno(), customerDTO.getCustTelno())) {
            detail.append(String.format("전화번호: %s → %s, ", originalCustomer.getCustTelno(), customerDTO.getCustTelno()));
        }
        if (!Objects.equals(originalCustomer.getCustEmail(), customerDTO.getCustEmail())) {
            detail.append(String.format("이메일: %s → %s, ", originalCustomer.getCustEmail(), customerDTO.getCustEmail()));
        }
        if (!Objects.equals(originalCustomer.getCustAddr(), customerDTO.getCustAddr())) {
            detail.append(String.format("주소: %s → %s, ", originalCustomer.getCustAddr(), customerDTO.getCustAddr()));
        }
        if (!Objects.equals(originalCustomer.getCustOccpTyCd(), customerDTO.getCustOccpTyCd())) {
            detail.append(String.format("직업 코드: %s → %s, ", originalCustomer.getCustOccpTyCd(), customerDTO.getCustOccpTyCd()));
        }

        // CustomerUpdateHistoryDTO 객체 생성
        CustomerUpdateHistoryDTO history = new CustomerUpdateHistoryDTO();
        history.setCustId(customerDTO.getCustId());
        history.setUserId(staffId); // 수정자 ID 설정
        history.setUpdateDetail(detail.length() > 0 ? detail.substring(0, detail.length() - 2) : "변경 사항 없음");
        history.setCustUpdateAt(LocalDateTime.now());

        return history;
    }

    public void saveHistory(CustomerUpdateHistoryDTO history) {
        customerMapper.insertUpdateHistory(history);
    }
}

