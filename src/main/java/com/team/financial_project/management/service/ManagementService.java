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

    public List<UserDTO> getManagementList(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<UserDTO> managementList = userMapper.selectManagementList(pageSize, offset);
        System.out.println("Management List: " + managementList);
        return managementList;
    }


    public int getTotalDataCount() {
        return userMapper.getTotalDataCount();
    }

//    public List<UserDTO> getManagementList() {
//
//        System.out.println("getManagementList >>");
//
//        return userMapper.selectAllManagementList();
//    }
}
