package com.team.financial_project.login.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class LoginService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder pwEncoder;

    public LoginService(UserMapper userMapper, BCryptPasswordEncoder pwEncoder) {
        this.userMapper = userMapper;
        this.pwEncoder = pwEncoder;
    }

    public boolean hasExistByUser_name(String userName, String userTelno) {
        UserDTO userDTO = userMapper.hasExistByUser_name(userName, userTelno);
        if(ObjectUtils.isEmpty(userDTO)){
            return false;
        }else {
            return true;
        }
    }

    public String findUserId(String userTelno) {
        return userMapper.findUserIdByUserTelno(userTelno);
    }

    public boolean hasExistByUser_id(String userId, String userTelno) {
        UserDTO userDTO = userMapper.hasExistByUser_id(userId, userTelno);
        if(ObjectUtils.isEmpty(userDTO)){
            return false;
        }else {
            return true;
        }
    }

    public boolean resetUserPw(String userId) {
        String resetPw = "a123456789";
        userMapper.updateUserPw(userId, pwEncoder.encode(resetPw));
        UserDTO userDTO = userMapper.findById(userId);
        return pwEncoder.matches(resetPw, userDTO.getUser_pw());
    }

    public boolean changeUserPw(String userId, String userPw) {
        userMapper.updateUserPw(userId,pwEncoder.encode(userPw));
        UserDTO userDTO = userMapper.findById(userId);
        return pwEncoder.matches(userPw,userDTO.getUser_pw());
    }
}
