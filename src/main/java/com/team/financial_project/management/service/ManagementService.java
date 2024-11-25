package com.team.financial_project.management.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementService {
    private final UserMapper userMapper;

    public ManagementService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 담당자 목록 조회
    public List<UserDTO> getManagersByName(String name) {
        return userMapper.findManagersByName(name);
    }
}
