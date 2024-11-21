package com.team.financial_project.management.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementService {
    private final UserMapper userMapper;

    public ManagementService(UserMapper userMapper){
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
    public List<UserDTO> getStatusList() { return userMapper.selectStatusList();
    }

    public List<UserDTO> getauthList() { return userMapper.selectAuthList();
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

}