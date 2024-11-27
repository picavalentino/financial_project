package com.team.financial_project.management.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagementService {
    private final UserMapper userMapper;

    public ManagementService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 셀렉트 박스 부서
    public List<UserDTO> getDepartmentList() {
        return userMapper.selectDepartmentList();
    }

    // 셀렉트 박스 직위
    public List<UserDTO> getJopPositionList() {
        return userMapper.selectJopPositionList();
    }

    // 셀렉트 박스 상태
    public List<UserDTO> getStatusList() {
        return userMapper.selectStatusList();
    }

    public List<UserDTO> getauthList() {
        return userMapper.selectAuthList();
    }

    // 전체 직원 수 가져오기 (검색 조건 포함)
    public int getTotalDataCount(String dept, String position, String searchField, String searchValue) {
        return userMapper.getTotalDataCount(dept, position, searchField, searchValue);
    }

    // 현재 페이지에 맞는 직원 목록 가져오기 (검색 조건 포함)
    public List<UserDTO> getManagementList(int page, int pageSize, String dept, String position, String searchField, String searchValue) {
        int offset = (page - 1) * pageSize;
        String validatedSearchField = null;

        if ("user_name".equals(searchField) || "user_email".equals(searchField) || "user_telno".equals(searchField)) {
            validatedSearchField = searchField;
        }
        System.out.println("검색 조건: dept=" + dept + ", position=" + position + ", searchField=" + searchField + ", searchValue=" + searchValue);
        return userMapper.selectManagementList(offset, pageSize, dept, position, validatedSearchField, searchValue);
    }

    // /employees 요청에서 전체 직원 수 가져오기 (검색 조건 포함)
    public int getTotalEmployeeCount(String dept, String position, String status, String searchField, String searchValue) {
        return userMapper.getTotalEmployeeCount(dept, position, status, searchField, searchValue);
    }

    // /employees 요청에서 현재 페이지에 맞는 직원 목록 가져오기 (검색 조건 포함)
    public List<UserDTO> getEmployeeList(int page, int pageSize, String dept, String position, String status, String auth, String searchField, String searchValue) {
        int offset = (page - 1) * pageSize;

        String validatedSearchField = null;
        if ("user_name".equals(searchField) || "user_id".equals(searchField)) {
            validatedSearchField = searchField;
        }

        System.out.println("검색 조건: dept=" + dept + ", position=" + position + ", status=" + status + ", auth=" + auth + ", searchField=" + validatedSearchField + ", searchValue=" + searchValue);
        return userMapper.selectEmployeeList(offset, pageSize, dept, position, status, auth, validatedSearchField, searchValue);
    }

    // insert 요청에서 신입사원 직원수 가져오기
    public int getTotalInsertCount(String yearMonth) {
        return userMapper.getTotalInsertCount(yearMonth);
    }

    public List<UserDTO> getInsertList(int page, int pageSize, String yearMonth) {
        int offset = (page - 1) * pageSize;

        System.out.println("검색 조건: yearMonth=" + yearMonth);
        return userMapper.selectInsertList(offset, pageSize, yearMonth);
    }

    // employee modal에서 받아 온 번호로 user 정보 찾기
    public UserDTO findByUserId(String userId) {
        return userMapper.selectUserById(userId);
    }

    // employee modal에서 user 정보 수정하기
    public void updateUser(UserDTO userDTO) {
        // 매퍼 또는 JPA 리포지토리를 이용해 사용자 정보 업데이트
        userMapper.updateUser(userDTO);
    }

    // 사원번호 생성
    @Transactional
    public int getNextSequenceForDate(String joiningDate) {
        // 데이터베이스에서 해당 입사일자에 맞는 등록된 마지막 사원번호를 찾고 시퀀스를 결정
        // 예를 들어, 'SELECT COUNT(*) FROM employees WHERE joining_date = #{joiningDate}' 를 통해 갯수를 가져와 +1
        int currentCount = userMapper.getEmployeeCountForDate(joiningDate);
        return currentCount + 1; // 시퀀스는 등록된 사원 수 + 1
    }

    // 직원 새로 등록하기
    @Transactional
    public void insertEmployee(UserDTO userDTO) {
        userMapper.insertEmployee(userDTO);
    }

    public int getTotalAuthCount(String auth) {
        return userMapper.getTotalAuthCount(auth);
    }

    public List<UserDTO> getauthList(int page, int pageSize, String auth) {
        int offset = (page - 1) * pageSize; // 오프셋 계산
        return userMapper.getauthList(offset, pageSize, auth); // Mapper 메소드 호출
    }
}
