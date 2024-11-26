package com.team.financial_project.login.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class LoginService {
    private final UserMapper userMapper;

    public LoginService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public boolean hasExist(String userName, String userTelno) {
        UserDTO userDTO = userMapper.hasExist(userName, userTelno);
        if(ObjectUtils.isEmpty(userDTO)){
            return false;
        }else {
            return true;
        }
    }

    public String findUserId(String userTelno) {
        return userMapper.findUserIdByUserTelno(userTelno);
    }
}
