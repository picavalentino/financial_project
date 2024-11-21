package com.team.financial_project.product.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<Map<String, Object>> findByNameContaining(String name) {
        return userMapper.findByNameContaining(name);
    }
}
