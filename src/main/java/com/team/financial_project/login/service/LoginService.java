package com.team.financial_project.login.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class LoginService {
    private final UserMapper userMapper;

    public LoginService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserDTO> findUsersByKeyword(String keyword) {
        return userMapper.findUsersByKeyword(keyword);
    }

    public boolean certifyByUserTelno(String telno) {
        UserDTO userDTO = userMapper.certifyByUserTelno(telno);
        if(ObjectUtils.isEmpty(userDTO)){
            return true;
        }else {
            return false;
        }
    }
}
