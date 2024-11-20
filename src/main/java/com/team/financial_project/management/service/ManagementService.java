package com.team.financial_project.management.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ManagementService {
    private final UserMapper userMapper;

    public ManagementService(UserMapper userMapper){
        this.userMapper = userMapper;
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


    public List<UserDTO> getDepartmentList() {
        return userMapper.selectDepartmentList();
    }

    public List<UserDTO> getJopPositionList() {
        return userMapper.selectJopPositionList();
    }
}
