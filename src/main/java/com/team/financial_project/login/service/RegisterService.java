package com.team.financial_project.login.service;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class RegisterService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder pwEncoder;

    public RegisterService(UserMapper userMapper, BCryptPasswordEncoder pwEncoder) {
        this.userMapper = userMapper;
        this.pwEncoder = pwEncoder;
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

    public void register(UserDTO user) {
        user.setUser_pw(pwEncoder.encode(user.getUser_pw()));
        userMapper.updateUserFromReg(user);
    }
}
